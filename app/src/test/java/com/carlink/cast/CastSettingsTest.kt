package com.carlink.cast

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CastSettingsTest {

    @Test
    fun audioDefaultsFollowAutomaticCarLifeSelection() {
        assertTrue(CastSettings.defaultAudioEnabled(SourceMode.AUTO))
        assertTrue(CastSettings.defaultAudioEnabled(SourceMode.CARLIFE))
        assertFalse(CastSettings.defaultAudioEnabled(SourceMode.MIRROR))
    }
}
