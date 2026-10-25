package com.carlink.cast

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import com.carplay.iap2.controller.WirelessEndpoint
import com.carplay.sender.PeerAddress
import com.carplay.sender.PeerAddresses
import java.io.Closeable
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/** Wi-Fi security as the head unit declared it in 0x4301. */
internal enum class WifiAuth { OPEN, WPA2, WPA3 }

internal object WifiAuthPolicy {
    /**
     * GM firmware reports a transition mode (4) that accepts either SAE or PSK;
     * every other value maps directly. Unknown values are refused rather than
     * guessed, because a wrong passphrase type fails only after a long timeout.
     */
    fun select(securityType: Int, supportsSae: Boolean): WifiAuth = when (securityType) {
        0 -> WifiAuth.OPEN
        2 -> WifiAuth.WPA2
        3 -> {
            check(supportsSae) { "车机要求 WPA3-SAE，当前手机不支持" }
            WifiAuth.WPA3
        }
        4 -> if (supportsSae) WifiAuth.WPA3 else WifiAuth.WPA2
        else -> error("车机提供了不支持的无线安全类型：$securityType")
    }
}

/**
 * The single vehicle Wi-Fi lease for one session.
 *
 * The request is bound to the SSID/passphrase from 0x4301; every socket the
 * session opens is bound to this Network so that a later default-network change
 * (cellular returning, the user joining another AP) can never move car traffic.
 * Loss of this specific network ends the session; loss of any other does not.
 */
internal class VehicleNetwork(
    private val context: Context,
    private val endpoint: WirelessEndpoint,
    private val log: (String) -> Unit,
) : Closeable {

    private val connectivity = context.getSystemService(ConnectivityManager::class.java)
    private val selected = AtomicBoolean()
    private val ended = AtomicBoolean()
    private val result = CompletableFuture<Network>()
    private val lease = java.util.concurrent.atomic.AtomicReference<Pair<Network, LinkProperties>?>()
    private var callbacks = mutableListOf<ConnectivityManager.NetworkCallback>()

    @Volatile var network: Network? = null
        private set

    @Volatile var link: LinkProperties? = null
        private set

    /**
     * Set when the vehicle lease disappears. Distinct from [close]: a loss is
     * the car going away, a close is this app ending the session on purpose.
     *
     * Without this flag nothing notices the car leaving. The session loop kept
     * spinning forever, CarLife kept rendering into a socket that went nowhere,
     * and CarLife went on holding the phone's audio route — which is why music
     * stayed silent until the app was force-stopped or the car was manually
     * disconnected from Honor's own UI.
     */
    @Volatile private var lost = false

    /** False once the car's network has gone away, so the session can stop. */
    fun isAlive(): Boolean = !ended.get() && !lost

    /** The phone's routable address on the vehicle network, preferring IPv4. */
    fun localAddress(): InetAddress {
        val addresses = link?.linkAddresses?.map { it.address }
            ?.filter { !it.isLoopbackAddress && !it.isAnyLocalAddress }
            .orEmpty()
        return addresses.firstOrNull { it is Inet4Address }
            ?: addresses.firstOrNull()
            ?: error("车机网络尚未分配有效地址")
    }

    /** Resolves the car's declared addresses against this network's own addresses. */
    fun resolveTargets(values: List<String>): List<PeerAddress> =
        PeerAddresses.resolve(values, link?.linkAddresses?.map { it.address }.orEmpty(), interfaceIndex())

    fun bind(socket: java.net.Socket) {
        val current = network ?: error("车机网络尚未就绪")
        current.bindSocket(socket)
    }

    fun bindDatagram(socket: java.net.DatagramSocket) {
        val current = network ?: error("车机网络尚未就绪")
        current.bindSocket(socket)
    }

    fun interfaceIndex(): Int =
        link?.interfaceName?.let { NetworkInterface.getByName(it)?.index } ?: 0

    fun isCurrent(value: Network) = network == value

    /** Joins the hotspot and waits for the address that matches what the car declared. */
    fun join(timeoutMillis: Long = 45_000): Network {
        val authentication = WifiAuthPolicy.select(endpoint.security,
            context.getSystemService(WifiManager::class.java).isWpa3SaeSupported)
        val spec = WifiNetworkSpecifier.Builder().setSsid(endpoint.ssid).apply {
            when (authentication) {
                WifiAuth.OPEN -> Unit
                WifiAuth.WPA2 -> setWpa2Passphrase(endpoint.password)
                WifiAuth.WPA3 -> setWpa3Passphrase(endpoint.password)
            }
        }.build()
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                if (ended.get()) return
                log("车机热点已连接：network=${network.networkHandle}")
            }

            override fun onLinkPropertiesChanged(network: Network, properties: LinkProperties) {
                if (ended.get()) return
                lease.set(network to properties)
                link = properties
                val addresses = properties.linkAddresses.map { it.address }
                val ready = if (endpoint.addresses.isEmpty()) {
                    addresses.any { !it.isLoopbackAddress && !it.isAnyLocalAddress }
                } else {
                    PeerAddresses.resolve(endpoint.addresses, addresses, interfaceIndexOf(properties)).isNotEmpty()
                }
                if (ready && selected.compareAndSet(false, true)) {
                    this@VehicleNetwork.network = network
                    log("车机网络地址就绪：interface=${properties.interfaceName}，" +
                        "IPv4=${addresses.any { it is Inet4Address }}，IPv6=${addresses.any { it is Inet6Address }}")
                    result.complete(network)
                }
            }

            override fun onUnavailable() {
                if (!ended.get()) result.completeExceptionally(IllegalStateException("车机热点连接被拒绝或超时"))
            }

            override fun onLost(network: Network) {
                if (ended.get() || this@VehicleNetwork.network != network) return
                // The car went out of range (or ended the hotspot). Flag it so
                // the running session tears itself down instead of spinning
                // forever with CarLife still holding the audio route.
                lost = true
                log("车机网络已断开：network=${network.networkHandle}")
                result.completeExceptionally(IllegalStateException("车机无线网络断开"))
            }
        }
        callbacks += callback
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .setNetworkSpecifier(spec).build()
        connectivity.requestNetwork(request, callback, timeoutMillis.toInt())
        log("正在加入车机热点 ${endpoint.ssid}（$authentication），等待车机声明的地址")
        return try {
            result.get(timeoutMillis + 10_000, TimeUnit.MILLISECONDS)
        } catch (failure: Exception) {
            close()
            throw if (failure is java.util.concurrent.ExecutionException) {
                failure.cause as? Exception ?: failure
            } else failure
        }
    }

    /** Keeps a default-network callback so the log explains route changes without obeying them. */
    fun observeDefaultNetwork() {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                if (!ended.get()) log("默认网络可用：network=${network.networkHandle}；车机连接不受影响")
            }

            override fun onLost(network: Network) {
                if (!ended.get()) log("默认网络变化：旧 network=${network.networkHandle}；车机连接保持")
            }
        }
        runCatching { connectivity.registerDefaultNetworkCallback(callback) }
            .onSuccess { callbacks += callback }
    }

    private fun interfaceIndexOf(properties: LinkProperties): Int =
        properties.interfaceName?.let { NetworkInterface.getByName(it)?.index } ?: 0

    override fun close() {
        if (!ended.compareAndSet(false, true)) return
        callbacks.forEach { runCatching { connectivity.unregisterNetworkCallback(it) } }
        callbacks.clear()
        network = null
        link = null
    }
}
