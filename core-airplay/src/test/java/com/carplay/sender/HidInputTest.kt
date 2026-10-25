package com.carplay.sender
import com.carplay.airplay.*
import org.junit.Assert.*
import org.junit.Test

class HidInputTest {
    @Test fun descriptorBasedTouchDecoderMatchesOriginalReceiverEncoder() {
        val descriptor = AirPlayHid.touchHidDevice(1920,720,"main")["hidDescriptor"] as ByteArray
        val decoder = HidInput(descriptor)
        val report = AirPlayHid.touchReport(listOf(AirPlayContact(0,960.0,360.0,true),AirPlayContact(1,1920.0,720.0,false)))
        val samples = decoder.touches(report)
        assertEquals(2,samples.size); assertEquals(0.5f,samples[0].x,0f); assertEquals(0.5f,samples[0].y,0f)
        assertTrue(samples[0].down); assertFalse(samples[1].down)
    }

    /**
     * A report too short for the descriptor must not throw.
     *
     * This head unit sends 5-byte touch reports for a descriptor whose finger
     * records span more bytes than that. Rejecting the report threw on every
     * touch, and the exception ended the command loop, so taps never reached
     * the phone. Undersized reports now decode to no contacts instead.
     */
    @Test fun shortReportYieldsNoContactsInsteadOfThrowing() {
        val descriptor = AirPlayHid.touchHidDevice(1920,720,"main")["hidDescriptor"] as ByteArray
        val decoder = HidInput(descriptor)
        assertTrue(decoder.touches(byteArrayOf(1)).isEmpty())
        assertTrue(decoder.touches(ByteArray(0)).isEmpty())
    }

    /** Whatever fits is still decoded, so a truncated report keeps its usable axes. */
    @Test fun truncatedReportStillDecodesTheFieldsThatFit() {
        val descriptor = AirPlayHid.touchHidDevice(1920,720,"main")["hidDescriptor"] as ByteArray
        val decoder = HidInput(descriptor)
        val full = AirPlayHid.touchReport(listOf(AirPlayContact(0,960.0,360.0,true)))
        // Drop the second finger record; the first contact must survive.
        val samples = decoder.touches(full.copyOf(6))
        assertEquals(1, samples.size)
        assertEquals(0.5f, samples[0].x, 0f)
        assertEquals(0.5f, samples[0].y, 0f)
        assertTrue(samples[0].down)
    }
    @Test fun geometryAccountsForLetterboxingAndRejectsTouchesOutsideContent() {
        val geometry = CaptureGeometry(1080,2160,1920,720)
        val center = geometry.point(960f,360f)!!
        assertEquals(540f,center.first,0.01f); assertEquals(1080f,center.second,0.01f)
        assertNull(geometry.point(10f,360f))
    }

    @Test fun singleReportIdIsStrippedBeforeTouchDecoding() {
        val descriptor = AirPlayHid.touchHidDevice(1920,720,"main")["hidDescriptor"] as ByteArray
        val decoder = HidInput(byteArrayOf(0x85.toByte(), 0x01) + descriptor)
        val report = AirPlayHid.touchReport(listOf(AirPlayContact(0,960.0,360.0,true)))
        val samples = decoder.touches(byteArrayOf(0x01) + report)
        // The fixed two-contact report includes the second slot as an
        // explicit up/empty contact; preserving it is required for release
        // events and matches the descriptor's contact count.
        assertEquals(2, samples.size)
        assertEquals(0.5f, samples[0].x, 0f)
        assertEquals(0.5f, samples[0].y, 0f)
        assertTrue(samples[0].down)
        assertFalse(samples[1].down)
        assertTrue(decoder.touches(byteArrayOf(0x02) + report).isEmpty())
    }
}
