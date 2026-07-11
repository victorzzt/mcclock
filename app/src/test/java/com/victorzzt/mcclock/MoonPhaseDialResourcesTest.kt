package com.victorzzt.mcclock

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MoonPhaseDialResourcesTest {
    @Test
    fun classicDialResourcesCoverEveryMoonPhase() {
        assertEquals(R.drawable.clock_dial_classic, MoonPhaseDialResources.classic.fallback)
        MoonPhase.entries.forEach { phase ->
            assertTrue(MoonPhaseDialResources.classic.dialFor(phase) != 0)
        }
    }

    @Test
    fun modernDialResourcesCoverEveryMoonPhase() {
        assertEquals(R.drawable.clock_dial_modern, MoonPhaseDialResources.modern.fallback)
        MoonPhase.entries.forEach { phase ->
            assertTrue(MoonPhaseDialResources.modern.dialFor(phase) != 0)
        }
    }
}
