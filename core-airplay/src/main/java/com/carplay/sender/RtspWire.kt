package com.carplay.sender

import com.carplay.airplay.ControlCipher
import java.io.Closeable
import java.io.EOFException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.util.ArrayDeque
import java.util.concurrent.ConcurrentHashMap

data class ControlMessage(val firstLine: String, val headers: Map<String, String>, val body: ByteArray) {
    val status: Int? get() = firstLine.split(' ').takeIf { it.first().startsWith("RTSP/") || it.first().startsWith("HTTP/") }?.getOrNull(1)?.toIntOrNull()
}
data class ControlReply(val status: Int, val body: ByteArray? = null)

object ControlFraming {
    data class Parsed(val messages: List<ControlMessage>, val rest: ByteArray)
    fun parse(bytes: ByteArray): Parsed {
        require(bytes.size <= 2_200_000) { "控制缓冲超过上限" }
        val messages = mutableListOf<ControlMessage>()
        var offset = 0
        while (offset < bytes.size) {
            var end = -1
            for (index in offset until bytes.size - 3) {
                if (bytes[index] == 13.toByte() && bytes[index + 1] == 10.toByte() && bytes[index + 2] == 13.toByte() && bytes[index + 3] == 10.toByte()) { end = index; break }
            }
            if (end == -1) { require(bytes.size - offset < 65536); break }
            require(end - offset < 65536)
            val lines = String(bytes, offset, end - offset, Charsets.US_ASCII).split("\r\n")
            val headers = linkedMapOf<String, String>()
            lines.drop(1).forEach {
                val separator = it.indexOf(':'); require(separator > 0) { "控制头无效" }
                val name = it.substring(0, separator).trim().lowercase()
                require(name !in headers) { "重复控制头：$name" }
                headers[name] = it.substring(separator + 1).trim()
            }
            val size = headers["content-length"]?.let { it.toIntOrNull() ?: error("Content-Length 无效") } ?: 0
            require(size in 0..2_000_000)
            if (bytes.size - end - 4 < size) break
            messages += ControlMessage(lines.first(), headers, bytes.copyOfRange(end + 4, end + 4 + size))
            offset = end + 4 + size
        }
        return Parsed(messages, bytes.copyOfRange(offset, bytes.size))
    }
}

/** A single reader; control requests and responses retain their sequence identity. */
class RtspWire(
    private val input: () -> InputStream,
    private val output: () -> OutputStream,
    private val release: () -> Unit,
    private val trace: (String) -> Unit = {},
) : Closeable {
    constructor(socket: Socket, trace: (String) -> Unit = {}) : this(socket::getInputStream,socket::getOutputStream,socket::close,trace)
    private var sequence = 0L
    private val sequenceLock = Any()
    private var plaintext = ByteArray(0)
    private var encrypted = ByteArray(0)
    private val pending = ArrayDeque<ControlMessage>()
    private val ignoredResponses = ConcurrentHashMap.newKeySet<String>()
    private val writeLock = Any()
    var cipher: ControlCipher? = null
    var onRequest: (ControlMessage) -> ControlReply = { ControlReply(501) }
    var onResponse: (ControlMessage) -> Unit = {}
    /**
     * Responses to fire-and-forget requests still carry useful payloads on
     * some head units. In particular, Buick can deliver the next iAP2 packet
     * on `/feedback` instead of the preceding `/command` response. The shared
     * reader must hand that body to the owner rather than discard it merely
     * because the CSeq was marked ignored.
     */
    var onIgnoredResponse: (ControlMessage) -> Unit = {}
    var session: String? = null

    /** Sends a command without reading its response; the channel's reader owns all reads. */
    @Synchronized fun sendCommand(body: ByteArray, headers: Map<String, String> = emptyMap()) {
        val seq = nextSequence()
        val all = linkedMapOf("CSeq" to seq.toString(), "User-Agent" to "AirPlay/320.17", "Content-Length" to body.size.toString())
        session?.let { all["Session"] = it }
        all.putAll(headers)
        write("POST /command RTSP/1.0", all, body)
    }

    /**
     * Writes a request whose response is deliberately ignored.
     *
     * The AirPlay receiver expects /feedback while a slow SETUP is in flight.
     * A normal [request] cannot be used from the feedback thread because it
     * waits for the same control reader that is waiting for SETUP. Marking the
     * CSeq lets that reader consume and discard the feedback reply safely.
     */
    fun sendAndForget(
        method: String,
        path: String,
        body: ByteArray = ByteArray(0),
        headers: Map<String, String> = emptyMap(),
    ) {
        require(method.matches(Regex("[A-Z_]+")) && '\r' !in path && '\n' !in path)
        val seq = nextSequence().toString()
        ignoredResponses += seq
        val all = linkedMapOf(
            "CSeq" to seq,
            "User-Agent" to "AirPlay/320.17",
            "Content-Length" to body.size.toString(),
        )
        session?.let { all["Session"] = it }
        all.putAll(headers)
        runCatching { write("$method $path RTSP/1.0", all, body) }
            .onFailure { ignoredResponses.remove(seq); throw it }
    }

    @Synchronized fun request(method: String, path: String, body: ByteArray = ByteArray(0), headers: Map<String, String> = emptyMap(), receive: ((ControlMessage) -> Unit)? = null): ControlMessage {
        require(method.matches(Regex("[A-Z_]+")) && '\r' !in path && '\n' !in path)
        val seq = nextSequence()
        val all = linkedMapOf("CSeq" to seq.toString(), "User-Agent" to "AirPlay/320.17", "Content-Length" to body.size.toString())
        session?.let { all["Session"] = it }
        all.putAll(headers)
        write("$method $path RTSP/1.0", all, body)
        while (true) {
            val message = next()
            if (message.status == null) { val reply = onRequest(message); respond(message,reply.body,reply.status); continue }
            val responseSequence = message.headers["cseq"].orEmpty()
            if (responseSequence != seq.toString()) {
                if (ignoredResponses.remove(responseSequence)) {
                    onIgnoredResponse(message)
                    continue
                }
                check(responseSequence == seq.toString()) { "控制响应序号不匹配" }
            }
            trace("$method $path → ${message.status}, body=${message.body.size}")
            check(message.status in 200..299) { "车机拒绝 $method $path：${message.status}" }
            message.headers["session"]?.let { session = it.substringBefore(';') }
            (receive ?: onResponse).invoke(message)
            return message
        }
    }

    fun next(): ControlMessage {
        while (pending.isEmpty()) {
            val bytes = ByteArray(8192)
            val count = input().read(bytes)
            if (count < 0) throw EOFException("控制通道关闭")
            val part = bytes.copyOf(count)
            val active = cipher
            if (active == null) plaintext += part else {
                encrypted += part
                require(encrypted.size <= 2_200_000)
                val result = active.decrypt(encrypted); encrypted = result.rest; plaintext += result.data
            }
            val parsed = ControlFraming.parse(plaintext); plaintext = parsed.rest; pending.addAll(parsed.messages)
        }
        return pending.removeFirst()
    }

    fun respond(request: ControlMessage, body: ByteArray?, status: Int = 200) {
        val protocol = when (request.firstLine.substringAfterLast(' ')) {
            "HTTP/1.0" -> "HTTP/1.0"
            "HTTP/1.1" -> "HTTP/1.1"
            else -> "RTSP/1.0"
        }
        val headers = linkedMapOf<String, String>()
        if (protocol == "RTSP/1.0" || "cseq" in request.headers) headers["CSeq"] = request.headers["cseq"].orEmpty()
        headers["Content-Length"] = (body?.size ?: 0).toString()
        if (body != null) headers["Content-Type"] = "application/x-apple-binary-plist"
        val reason = when (status) { 200 -> "OK"; 400 -> "Bad Request"; 403 -> "Forbidden"; else -> "Not Implemented" }
        write("$protocol $status $reason", headers, body ?: ByteArray(0))
    }

    private fun write(firstLine: String, headers: Map<String, String>, body: ByteArray) = synchronized(writeLock) {
        headers.forEach { (k, v) -> require('\r' !in k && '\n' !in k && '\r' !in v && '\n' !in v) }
        val head = buildString { append(firstLine).append("\r\n"); headers.forEach { (k,v) -> append(k).append(": ").append(v).append("\r\n") }; append("\r\n") }.toByteArray(Charsets.US_ASCII)
        val bytes = head + body
        output().write(cipher?.encrypt(bytes) ?: bytes); output().flush()
    }
    private fun nextSequence(): Long = synchronized(sequenceLock) { ++sequence }
    override fun close() { release() }
}
