package com.carlink.cast

import android.content.Context
import android.net.Network
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build
import android.system.Os
import android.system.OsConstants
import android.system.StructPollfd
import java.io.Closeable
import java.io.IOException
import java.net.Inet4Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceInfo
import javax.jmdns.ServiceListener

/**
 * Finds the car's AirPlay endpoint on the vehicle network.
 *
 * 0x4301 does not always carry the AirPlay address on this firmware, so the
 * phone also publishes its control service and discovers `_airplay._tcp`, the
 * same way the captured iPhone session does. Discovery is scoped to the
 * vehicle network on Android 13+; older releases use jmDNS bound to the
 * vehicle address because NsdManager would otherwise use the default network.
 */
internal class AirPlayDiscovery(
    private val context: Context,
    private val network: Network,
    private val local: InetAddress,
    private val controlPort: Int,
    private val transportId: String,
    private val log: (String) -> Unit,
) : Closeable {

    data class Found(val addresses: List<InetAddress>, val port: Int, val publicKey: String?)

    private val closed = AtomicBoolean()
    private val result = CompletableFuture<Found>()
    private val manager = context.getSystemService(NsdManager::class.java)
    private var legacy: JmDNS? = null
    private var registered = false
    private var discovering = false
    private val resolving = ConcurrentHashMap.newKeySet<String>()
    private val executor = Executor { it.run() }

    private val registration = object : NsdManager.RegistrationListener {
        override fun onServiceRegistered(info: NsdServiceInfo) {
            if (closed.get()) { runCatching { manager.unregisterService(this) }; return }
            log("控制服务已发布到车机网络：${info.serviceName}")
        }
        override fun onRegistrationFailed(info: NsdServiceInfo, code: Int) {
            registered = false
            log("控制服务发布失败：$code")
        }
        override fun onServiceUnregistered(info: NsdServiceInfo) = Unit
        override fun onUnregistrationFailed(info: NsdServiceInfo, code: Int) = Unit
    }

    private val listener = object : NsdManager.DiscoveryListener {
        override fun onDiscoveryStarted(type: String) {
            log("开始发现车机 AirPlay 服务")
        }
        override fun onDiscoveryStopped(type: String) = Unit
        override fun onStartDiscoveryFailed(type: String, code: Int) {
            discovering = false
            if (!closed.get()) result.completeExceptionally(IOException("Bonjour 发现失败：$code"))
        }
        override fun onStopDiscoveryFailed(type: String, code: Int) = Unit
        override fun onServiceLost(info: NsdServiceInfo) = Unit
        override fun onServiceFound(info: NsdServiceInfo) {
            if (closed.get() || resolving.size >= 8 || !resolving.add(info.serviceName)) return
            val request = NsdServiceInfo().apply {
                serviceName = info.serviceName
                serviceType = info.serviceType
                if (Build.VERSION.SDK_INT >= 33) setNetwork(network)
            }
            @Suppress("DEPRECATION")
            runCatching {
                manager.resolveService(request, object : NsdManager.ResolveListener {
                    override fun onResolveFailed(service: NsdServiceInfo, code: Int) {
                        resolving.remove(info.serviceName)
                    }
                    override fun onServiceResolved(service: NsdServiceInfo) {
                        resolving.remove(info.serviceName)
                        if (closed.get()) return
                        if (Build.VERSION.SDK_INT >= 33 && service.network != network) return
                        @Suppress("DEPRECATION")
                        val addresses = if (Build.VERSION.SDK_INT >= 34) service.hostAddresses else listOfNotNull(service.host)
                        deliver(addresses, service.port, service.attributes)
                    }
                })
            }.onFailure { resolving.remove(info.serviceName) }
        }
    }

    private fun deliver(addresses: List<InetAddress>, port: Int, attributes: Map<String, ByteArray>) {
        if (port !in 1..65535 || addresses.isEmpty()) return
        val key = attributes["pk"]?.toString(Charsets.UTF_8)
        if (result.complete(Found(addresses, port, key))) {
            log("已发现车机 AirPlay：${addresses.first().hostAddress}:$port")
        }
    }

    fun start() {
        if (Build.VERSION.SDK_INT >= 33) {
            val info = NsdServiceInfo().apply {
                serviceName = "CarLink Cast"
                serviceType = "_carplay-ctrl._tcp"
                setPort(controlPort)
                setNetwork(network)
                setAttribute("id", transportId)
                setAttribute("srcvers", "320.17")
            }
            registered = true
            runCatching { manager.registerService(info, NsdManager.PROTOCOL_DNS_SD, registration) }
                .onFailure { registered = false; log("控制服务发布异常：${it.message}") }
            discovering = true
            runCatching {
                manager.discoverServices("_airplay._tcp", NsdManager.PROTOCOL_DNS_SD, network, executor, listener)
            }.onFailure {
                discovering = false
                log("Bonjour 发现异常：${it.message}")
            }
        } else {
            val dns = JmDNS.create(local, "CarLink Cast")
            legacy = dns
            runCatching {
                dns.registerService(ServiceInfo.create("_carplay-ctrl._tcp.local.", "CarLink Cast",
                    controlPort, 0, 0, mapOf("id" to transportId, "srcvers" to "320.17")))
            }.onFailure { log("控制服务发布异常：${it.message}") }
            dns.addServiceListener("_airplay._tcp.local.", object : ServiceListener {
                override fun serviceAdded(event: ServiceEvent) {
                    if (!closed.get()) dns.requestServiceInfo(event.type, event.name, true)
                }
                override fun serviceRemoved(event: ServiceEvent) = Unit
                override fun serviceResolved(event: ServiceEvent) {
                    if (closed.get()) return
                    val info = event.info
                    deliver(info.inetAddresses.toList(), info.port,
                        info.propertyNames.toList().associateWith { info.getPropertyBytes(it) ?: ByteArray(0) })
                }
            })
            log("Bonjour 使用车机网络本地地址（Android 10–12）")
        }
    }

    /** Waits for the car's AirPlay service, or null if it was already known. */
    fun await(timeoutMillis: Long = 20_000): Found? = try {
        result.get(timeoutMillis, TimeUnit.MILLISECONDS)
    } catch (_: Exception) {
        null
    }

    override fun close() {
        if (!closed.compareAndSet(false, true)) return
        if (Build.VERSION.SDK_INT >= 33) {
            if (discovering) runCatching { manager.stopServiceDiscovery(listener) }
            if (registered) runCatching { manager.unregisterService(registration) }
        }
        runCatching { legacy?.close() }
        legacy = null
    }
}

/**
 * Accepts the car's control invitation (`GET /ctrl-int/1/connect`).
 *
 * The socket is created with Os so it can be bound to the vehicle Network, the
 * same lease every other session socket uses. The invitation itself is only an
 * acknowledgement; the authenticated iAP control stream remains type 130.
 */
internal class ControlInvitationServer(
    private val network: Network,
    private val local: InetAddress,
    private val log: (String) -> Unit,
) : Closeable {

    private val closed = AtomicBoolean()
    private val descriptor = Os.socket(
        if (local is java.net.Inet6Address) OsConstants.AF_INET6 else OsConstants.AF_INET,
        OsConstants.SOCK_STREAM, OsConstants.IPPROTO_TCP)
    val port: Int
    private var thread: Thread? = null

    init {
        try {
            network.bindSocket(descriptor)
            Os.setsockoptInt(descriptor, OsConstants.SOL_SOCKET, OsConstants.SO_REUSEADDR, 1)
            Os.bind(descriptor, local, 0)
            Os.listen(descriptor, 8)
            port = (Os.getsockname(descriptor) as InetSocketAddress).port
        } catch (failure: Exception) {
            runCatching { Os.close(descriptor) }
            throw IOException("控制邀请监听创建失败", failure)
        }
    }

    fun start() {
        thread = Thread({
            val buffer = ByteArray(4096)
            while (!closed.get()) {
                val poll = StructPollfd().apply {
                    fd = descriptor
                    events = OsConstants.POLLIN.toShort()
                }
                val ready = runCatching { Os.poll(arrayOf(poll), 1000) }.getOrDefault(0)
                if (ready <= 0) continue
                val accepted = runCatching { Os.accept(descriptor, null) }.getOrNull() ?: continue
                runCatching {
                    java.io.FileInputStream(accepted).use { input ->
                        val count = input.read(buffer)
                        val request = if (count > 0) String(buffer, 0, count, Charsets.US_ASCII) else ""
                        val acceptedInvite = request.startsWith("GET /ctrl-int/1/connect")
                        val status = if (acceptedInvite) "200 OK" else "400 Bad Request"
                        java.io.FileOutputStream(accepted).use { output ->
                            output.write("HTTP/1.1 $status\r\nContent-Length: 0\r\n\r\n".toByteArray())
                            output.flush()
                        }
                        if (acceptedInvite) log("收到车机控制连接邀请")
                    }
                }.onFailure { log("控制邀请处理失败：${it.message}") }
                runCatching { Os.close(accepted) }
            }
        }, "carlink-invitation").apply { isDaemon = true; start() }
    }

    override fun close() {
        if (!closed.compareAndSet(false, true)) return
        runCatching { Os.shutdown(descriptor, OsConstants.SHUT_RDWR) }
        runCatching { Os.close(descriptor) }
        thread?.interrupt()
    }
}
