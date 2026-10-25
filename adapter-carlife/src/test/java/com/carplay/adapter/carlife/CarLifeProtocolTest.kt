package com.carplay.adapter.carlife

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Wire-format tests built from frames captured off the Honor CarLife's own
 * sockets, so a regression in the framing shows up without a car attached.
 */
class CarLifeProtocolTest {

    private fun hex(value: String): ByteArray =
        value.chunked(2).map { it.toInt(16).toByte() }.toByteArray()

    @Test
    fun `restore during initial encoder startup waits for a later screen request`() {
        val lifecycle = CarLifeEncoderLifecycle()

        assertTrue(lifecycle.requestInitial())
        assertFalse(lifecycle.requestRebuild())
        assertFalse(lifecycle.requestRebuild())
        assertTrue(lifecycle.onEncoderInitDone())
        assertFalse(lifecycle.onEncoderStarted())
        assertTrue(lifecycle.requestRebuild())
        assertTrue(lifecycle.onEncoderInitDone())
        assertFalse(lifecycle.onEncoderStarted())
        assertFalse(lifecycle.onEncoderInitDone())
    }

    @Test
    fun `restore after startup starts one rebuild`() {
        val lifecycle = CarLifeEncoderLifecycle()

        assertTrue(lifecycle.requestInitial())
        assertTrue(lifecycle.onEncoderInitDone())
        assertFalse(lifecycle.onEncoderStarted())
        assertTrue(lifecycle.requestRebuild())
        assertFalse(lifecycle.requestRebuild())
        assertTrue(lifecycle.onEncoderInitDone())
        assertFalse(lifecycle.onEncoderStarted())
    }

    @Test
    fun `reset discards queued restore requests`() {
        val lifecycle = CarLifeEncoderLifecycle()

        assertFalse(lifecycle.requestRebuild())
        lifecycle.reset()

        assertFalse(lifecycle.onEncoderInitDone())
        assertTrue(lifecycle.requestInitial())
        assertTrue(lifecycle.onEncoderInitDone())
        assertFalse(lifecycle.onEncoderStarted())
    }

    // --- command channel ---

    @Test
    fun `decodes a captured version match frame`() {
        // 0002 0000 00010002 0801 -> length 2, id 0x00010002, payload {field1: 1}
        val frames = CarLifeCommandSplitter().feed(hex("00020000000100020801"), 0, 10)
        assertEquals(1, frames.size)
        assertEquals(0x00010002, 0x00010000 or (frames[0].serviceType and 0x7FFF))
        assertArrayEquals(hex("0801"), frames[0].payload)
        assertEquals(1, CarLifeProto.decodeMatchStatus(frames[0].payload))
    }

    @Test
    fun `decodes a captured MD_INFO frame with its full 350 byte payload`() {
        val payload = ByteArray(350) { 0x41 }
        val frame = CarLifeProtocol.header(CarLifeProtocol.CHANNEL_CMD, CarLifeProtocol.MD_INFO, payload.size, 0) + payload
        // Header must be the 8-byte command form: [u16 len][u16 rsv][u32 id].
        assertArrayEquals(hex("015e000000010004"), frame.copyOfRange(0, 8))

        val frames = CarLifeCommandSplitter().feed(frame, 0, frame.size)
        assertEquals(1, frames.size)
        assertEquals(350, frames[0].payload.size)
    }

    @Test
    fun `splits frames that arrive joined and split across reads`() {
        val first = CarLifeProtocol.header(CarLifeProtocol.CHANNEL_CMD, CarLifeProtocol.PROTOCOL_VERSION_MATCH_STATUS, 2, 0) + hex("0801")
        val second = CarLifeProtocol.header(CarLifeProtocol.CHANNEL_CMD, CarLifeProtocol.MD_INFO, 3, 0) + hex("0a0741")
        val joined = first + second

        // Split mid-header and mid-payload: the splitter must hold the remainder.
        val splitter = CarLifeCommandSplitter()
        assertTrue(splitter.feed(joined, 0, 5).isEmpty())
        val tail = splitter.feed(joined, 5, joined.size - 5)
        assertEquals(2, tail.size)
        assertEquals(2, tail[0].payload.size)
        assertEquals(3, tail[1].payload.size)
    }

    @Test
    fun `an incomplete frame is held rather than misread`() {
        // A command frame whose payload has not fully arrived must stay buffered.
        // The length is a u16, so it can never exceed the protocol ceiling; the
        // protection here is waiting, not rejecting.
        val complete = CarLifeProtocol.header(CarLifeProtocol.CHANNEL_CMD, CarLifeProtocol.MD_INFO, 350, 0) + ByteArray(350)
        assertEquals(358, complete.size)

        val splitter = CarLifeCommandSplitter()
        assertTrue(splitter.feed(complete, 0, 8).isEmpty())     // header only
        assertTrue(splitter.feed(complete, 8, 349).isEmpty())   // all but the last payload byte
        assertEquals(1, splitter.feed(complete, 357, 1).size)   // the final byte completes it
    }

    // --- video channel ---

    @Test
    fun `decodes a captured video packet`() {
        // 00002005 6ad26d9d 00020001 <H.264>: length 8197, timestamp, then SPS.
        // 8197 payload plus the 12-byte header is exactly the 8209 bytes the phone
        // actually sent for its first packet.
        val prefix = hex("000000016742801fda01001269a8283028368509a8")
        val nal = prefix + ByteArray(8197 - prefix.size)
        val packet = hex("00002005") + hex("6ad26d9d") + hex("00020001") + nal
        assertEquals(8209, packet.size)

        val packets = CarLifeStreamSplitter().feed(packet, 0, packet.size)
        assertEquals(1, packets.size)
        assertEquals(CarLifeProtocol.STREAM_VIDEO_DATA, packets[0].serviceType)
        assertEquals(8197, packets[0].data.size)
        assertEquals(0x6ad26d9dL, packets[0].timestampSeconds)
        // Starts on the Annex-B start code so the access unit is self-contained.
        assertArrayEquals(hex("00000001"), packets[0].data.copyOfRange(0, 4))
        assertEquals(7, packets[0].data[4].toInt() and 0x1F) // SPS
    }

    @Test
    fun `SPS and PPS are not key frames but an IDR is`() {
        val sps = hex("000000016742801fda01")
        val pps = hex("0000000168ce06f2")
        val idr = hex("0000000165b8427f")

        assertFalse(isCarLifeKeyFrame(sps))
        assertFalse(isCarLifeKeyFrame(sps + pps))
        assertTrue(isCarLifeKeyFrame(sps + pps + idr))
    }

    @Test
    fun `IDR detection accepts three byte Annex-B start codes`() {
        assertFalse(isCarLifeKeyFrame(hex("0000016742801f")))
        assertTrue(isCarLifeKeyFrame(hex("00000165b8427f")))
    }

    @Test
    fun `non-IDR slices are not key frames`() {
        assertFalse(isCarLifeKeyFrame(hex("0000000141b8427f")))
    }

    @Test
    fun `video keep-alive packets are consumed without desynchronising the stream`() {
        // The encoder sends a zero-length header (marker 00 02 00 01) when it has
        // no new frame, and 00 02 00 02 while backgrounded. A zero length used to
        // advance one byte at a time, which shifted every later boundary and made
        // the car show one second of video and then black.
        val keepAlive = hex("00000000") + hex("6ad26d9d") + hex("00020001")
        val background = hex("00000000") + hex("6ad26d9e") + hex("00020002")
        val startCode = hex("000000016742801fda01")
        val nal = startCode + ByteArray(120 - startCode.size)
        val frame = hex("00000078") + hex("6ad26d9f") + hex("00020001") + nal

        val splitter = CarLifeStreamSplitter()
        val packets = splitter.feed(keepAlive + background + frame, 0, keepAlive.size + background.size + frame.size)
        assertEquals(1, packets.size)
        assertEquals(CarLifeProtocol.STREAM_VIDEO_DATA, packets[0].serviceType)
        assertEquals(120, packets[0].data.size)
        assertEquals(0x6ad26d9fL, packets[0].timestampSeconds)
    }

    @Test
    fun `media packets decode their service id and audio init`() {
        val init = CarLifeProto.Writer().int32(1, 48_000).int32(2, 2).int32(3, 16).toByteArray()
        val packet = CarLifeProtocol.header(CarLifeProtocol.CHANNEL_MEDIA, CarLifeProtocol.STREAM_MEDIA_INIT, init.size, 0) + init
        val packets = CarLifeStreamSplitter().feed(packet, 0, packet.size)
        assertEquals(1, packets.size)
        assertEquals(CarLifeProtocol.STREAM_MEDIA_INIT, packets[0].serviceType)
        val (rate, channels, format) = CarLifeProto.decodeAudioInit(packets[0].data)
        assertEquals(48_000, rate)
        assertEquals(2, channels)
        assertEquals(16, format)
    }

    @Test
    fun `video splitter keeps a partial packet for the next read`() {
        // Length 0x78 = 120 payload bytes, plus the 12-byte header.
        val startCode = hex("000000016742801fda01")
        assertEquals(10, startCode.size)
        val nal = startCode + ByteArray(120 - startCode.size)
        val packet = hex("00000078") + hex("6ad26d9d") + hex("00020001") + nal
        assertEquals(132, packet.size)

        val splitter = CarLifeStreamSplitter()
        assertTrue(splitter.feed(packet, 0, 20).isEmpty())
        val done = splitter.feed(packet, 20, packet.size - 20)
        assertEquals(1, done.size)
        assertEquals(120, done[0].data.size)
    }

    // --- protobuf ---

    @Test
    fun `video encoder init round trips through the phone's own decoder shape`() {
        // The phone parses width=1, height=2, frameRate=3 from this payload.
        val encoded = CarLifeProto.videoEncoderInfo(1024, 576, 30)
        assertArrayEquals(hex("08800810c004181e"), encoded)

        val (w, h, fps) = CarLifeProto.decodeVideoEncoderInfo(encoded)
        assertEquals(1024, w)
        assertEquals(576, h)
        assertEquals(30, fps)
    }

    @Test
    fun `video encoder init requests full panel adaptation`() {
        val encoded = CarLifeProto.videoEncoderInfo(
            width = 1408,
            height = 476,
            frameRate = 30,
            areaWidth = 1408,
            areaHeight = 476,
            areaX = 0,
            areaY = 0,
            drawOutsideSafeArea = false,
            needAdapterScreen = true,
        )

        assertArrayEquals(
            hex("08800b10dc03181e20800b28dc034801"),
            encoded,
        )
    }

    @Test
    fun `video encoder init carries buick safe area origin`() {
        val encoded = CarLifeProto.videoEncoderInfo(
            width = 1972,
            height = 520,
            frameRate = 30,
            areaWidth = 1408,
            areaHeight = 476,
            areaX = 278,
            areaY = 44,
            drawOutsideSafeArea = false,
            needAdapterScreen = true,
        )

        assertArrayEquals(
            hex("08b40f108804181e20800b28dc03309602382c4801"),
            encoded,
        )
        assertEquals(Triple(1972, 520, 30), CarLifeProto.decodeVideoEncoderInfo(encoded))
    }

    @Test
    fun `panel touch preserves the safe-view normalized position without subtracting its origin`() {
        val panel = CarLifePanel(
            panelWidth = 1972,
            panelHeight = 520,
            safeX = 278,
            safeY = 44,
            safeWidth = 1408,
            safeHeight = 476,
        )

        val mapped = panel.mapTouchToSource(0.75f * 1972, 0.75f * 520, 1408, 476)

        assertNotNull(mapped)
        assertEquals(1056f, mapped!!.first, 0.01f)
        assertEquals(357f, mapped.second, 0.01f)
        assertNull(panel.mapTouchToSource(1972f, 519f, 1408, 476))
    }

    @Test
    fun `decodes the authen response string`() {
        val payload = CarLifeProto.Writer().string(1, "8.8.4_8.8.4").toByteArray()
        assertEquals("8.8.4_8.8.4", CarLifeProto.decodeAuthenResponse(payload))
    }

    @Test
    fun `builds the exact authen request the phone expects`() {
        // 0a0b "8.8.4_8.8.4": field 1, length 11, then the challenge string.
        assertArrayEquals(hex("0a0b382e382e345f382e382e34"), CarLifeProto.authenRequest("8.8.4_8.8.4"))
    }

    @Test
    fun `touch point encodes x and y as fields one and two`() {
        assertArrayEquals(hex("08e70710d204"), CarLifeProto.touchPoint(999, 594))
    }

    @Test
    fun `feature config carries a count and key value pairs`() {
        val key = "DISABLE_RESOLUTION_OPTION"
        val payload = CarLifeProto.featureConfig(key to 1)
        // 08 01                          -> field 1 (cnt), varint 1
        // 12 <len> <0a len key 10 01>    -> field 2, one nested CarlifeFeatureConfig
        assertEquals(0x08, payload[0].toInt())
        assertEquals(1, payload[1].toInt())
        assertEquals(0x12, payload[2].toInt())
        assertEquals(key.length + 4, payload[3].toInt())

        val reader = CarLifeProto.Reader(payload)
        assertEquals(1, reader.nextField()); assertEquals(1, reader.readInt32())
        assertEquals(2, reader.nextField())
        val nested = CarLifeProto.Reader(reader.readBytes())
        assertEquals(1, nested.nextField()); assertEquals(key, nested.readString())
        assertEquals(2, nested.nextField()); assertEquals(1, nested.readInt32())
    }

    // --- avcC assembly ---

    @Test
    fun `builds avcC from an access unit carrying SPS and PPS`() {
        // Shaped like the phone's first packet: SPS, PPS, then an IDR slice.
        val sps = hex("000000016742801fda01001269a8283028368509a8")
        val pps = hex("0000000168ce06f2")
        val idr = hex("0000000165b8427ffc3c7140004206ef2727")
        val config = CarLifeProto.avcConfig(sps + pps + idr)

        assertTrue("avcC should be produced", config != null)
        val c = config!!
        assertEquals(1, c[0].toInt())            // configurationVersion
        assertEquals(0x42, c[1].toInt() and 0xFF) // AVCProfileIndication from the SPS
        assertEquals(0x80, c[2].toInt() and 0xFF) // profile_compatibility
        assertEquals(0x1f, c[3].toInt() and 0xFF) // AVCLevelIndication
        assertEquals(0xff, c[4].toInt() and 0xFF) // 6 bits reserved + lengthSizeMinusOne
        assertEquals(0xe1, c[5].toInt() and 0xFF) // 3 bits reserved + numOfSequenceParameterSets
        // The parameter sets are stored without their start codes.
        assertEquals(sps.size - 4, ((c[6].toInt() and 0xFF) shl 8) or (c[7].toInt() and 0xFF))
        assertArrayEquals(sps.copyOfRange(4, sps.size), c.copyOfRange(8, 8 + sps.size - 4))
    }

    @Test
    fun `avcC is withheld until both parameter sets have arrived`() {
        // A slice-only access unit cannot describe the stream yet.
        assertNull(CarLifeProto.avcConfig(hex("0000000165b8427f")))
        assertNull(CarLifeProto.avcConfig(hex("000000016742801fda01")))
    }

    @Test
    fun `config collector combines SPS and PPS from separate access units`() {
        val collector = CarLifeAvcConfigCollector()
        val sps = hex("000000016742801fda01001269a8283028368509a8")
        val pps = hex("0000000168ce06f2")

        assertNull(collector.offer(sps))
        val config = collector.offer(pps)

        assertNotNull(config)
        assertEquals(0x42, config!![1].toInt() and 0xFF)
    }

    @Test
    fun `config collector accepts three-byte Annex B start codes`() {
        val collector = CarLifeAvcConfigCollector()
        val sps = hex("0000016742801fda01001269a8283028368509a8")
        val pps = hex("00000168ce06f2")

        assertNotNull(collector.offer(sps + pps))
    }

    @Test
    fun `config collector reset drops stale parameter sets`() {
        val collector = CarLifeAvcConfigCollector()
        val sps = hex("000000016742801fda01001269a8283028368509a8")
        val pps = hex("0000000168ce06f2")

        assertNull(collector.offer(sps))
        collector.reset()

        assertNull(collector.offer(pps))
    }

    // --- frame header ---

    @Test
    fun `command and touch frames use the short header`() {
        assertEquals(8, CarLifeProtocol.header(CarLifeProtocol.CHANNEL_CMD, 0, 0, 0).size)
        assertEquals(8, CarLifeProtocol.header(CarLifeProtocol.CHANNEL_TOUCH, 0, 0, 0).size)
        // Media and the rest carry a 12-byte header with a timestamp.
        assertEquals(12, CarLifeProtocol.header(CarLifeProtocol.CHANNEL_MEDIA, 0, 0, 0).size)
        assertEquals(12, CarLifeProtocol.header(CarLifeProtocol.CHANNEL_VIDEO, 0, 0, 0).size)
    }

    @Test
    fun `all counted sockets are required while Data remains optional`() {
        assertEquals(
            setOf(
                CarLifeProtocol.PORT_VIDEO,
                CarLifeProtocol.PORT_MEDIA,
                CarLifeProtocol.PORT_TTS,
                CarLifeProtocol.PORT_VR,
                CarLifeProtocol.PORT_TOUCH,
            ),
            CarLifeProtocol.REQUIRED_AUX_PORTS,
        )
        assertFalse(CarLifeProtocol.PORT_DATA in CarLifeProtocol.REQUIRED_AUX_PORTS)
    }

    @Test
    fun `touch header carries the service id in the low four bytes`() {
        val header = CarLifeProtocol.header(CarLifeProtocol.CHANNEL_TOUCH, CarLifeProtocol.MSG_TOUCH_ACTION_DOWN, 6, 0)
        // 425986 = 0x00068002, written big-endian after the 2-byte length and 2-byte reserved.
        assertArrayEquals(hex("0006000000068002"), header)
    }
}
