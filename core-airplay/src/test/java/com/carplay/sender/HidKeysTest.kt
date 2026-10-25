package com.carplay.sender

import com.carplay.airplay.*
import org.junit.Assert.*
import org.junit.Test

class HidKeysTest {
    @Test fun originalMediaSelectorsProducePressReleaseWithoutDuplicatePresses() {
        val keys = HidKeys(AirPlayHid.mediaHidDevice("main")["hidDescriptor"] as ByteArray)
        assertEquals(listOf(HidKeyChange(HidUsage(0x0c,0xb0),true)),keys.report(AirPlayHid.mediaReport(1)).changes)
        assertTrue(keys.report(AirPlayHid.mediaReport(1)).changes.isEmpty())
        assertEquals(listOf(HidKeyChange(HidUsage(0x0c,0xb0),false),HidKeyChange(HidUsage(0x0c,0xb1),true)),keys.report(AirPlayHid.mediaReport(2)).changes)
        assertEquals(listOf(HidKeyChange(HidUsage(0x0c,0xb1),false)),keys.report(AirPlayHid.mediaReport(0)).changes)
        assertThrows(IllegalArgumentException::class.java) { keys.report(byteArrayOf(99)) }
    }
    @Test fun originalKnobIsNotDecodedAsTouchAndPreservesSignedWheel() {
        val descriptor = AirPlayHid.knobHidDevice("main")["hidDescriptor"] as ByteArray
        assertFalse(HidInput(descriptor).isTouch)
        val keys = HidKeys(descriptor)
        val report = keys.report(AirPlayHid.knobReport(AirPlayKnobState(home=true,back=true,wheel=-3)))
        assertEquals(-3,report.wheel)
        assertEquals(setOf(HidUsage(0x0c,0x223),HidUsage(0x0c,0x224)),report.held)
    }

    @Test fun singleReportIdIsStrippedBeforeDecoding() {
        val descriptor = AirPlayHid.mediaHidDevice("main")["hidDescriptor"] as ByteArray
        val withReportId = byteArrayOf(0x85.toByte(), 0x01) + descriptor
        val keys = HidKeys(withReportId)
        val report = keys.report(byteArrayOf(0x01, 0x01))
        assertEquals(listOf(HidKeyChange(HidUsage(0x0c, 0xb0), true)), report.changes)
        assertThrows(IllegalArgumentException::class.java) {
            keys.report(byteArrayOf(0x02, 0x01))
        }
    }
}
