package com.victorzzt.mcclock

import org.junit.Assert.assertEquals
import org.junit.Test

class MoonPhaseTest {
    @Test
    fun epochIsNewMoon() {
        assertEquals(MoonPhase.NEW_MOON, MoonPhase.fromMillis(MoonPhase.EPOCH_MILLIS))
    }

    @Test
    fun phaseFractionsPreserveExistingBuckets() {
        assertEquals(MoonPhase.NEW_MOON, phaseAt(0.01))
        assertEquals(MoonPhase.WAXING_CRESCENT, phaseAt(0.10))
        assertEquals(MoonPhase.FIRST_QUARTER, phaseAt(0.25))
        assertEquals(MoonPhase.WAXING_GIBBOUS, phaseAt(0.35))
        assertEquals(MoonPhase.FULL_MOON, phaseAt(0.50))
        assertEquals(MoonPhase.WANING_GIBBOUS, phaseAt(0.60))
        assertEquals(MoonPhase.LAST_QUARTER, phaseAt(0.75))
        assertEquals(MoonPhase.WANING_CRESCENT, phaseAt(0.90))
        assertEquals(MoonPhase.NEW_MOON, phaseAt(0.95))
    }

    @Test
    fun datesBeforeEpochNormalizeIntoPreviousCycle() {
        assertEquals(MoonPhase.NEW_MOON, MoonPhase.fromMillis(MoonPhase.EPOCH_MILLIS - 1))
    }

    @Test
    fun legacyNamesMatchOldClockUtilsReturnValues() {
        assertEquals("New_Moon", MoonPhase.NEW_MOON.legacyName)
        assertEquals("Waxing_Crescent", MoonPhase.WAXING_CRESCENT.legacyName)
        assertEquals("First_Quarter", MoonPhase.FIRST_QUARTER.legacyName)
        assertEquals("Waxing_Gibbous", MoonPhase.WAXING_GIBBOUS.legacyName)
        assertEquals("Full_Moon", MoonPhase.FULL_MOON.legacyName)
        assertEquals("Waning_Gibbous", MoonPhase.WANING_GIBBOUS.legacyName)
        assertEquals("Last_Quarter", MoonPhase.LAST_QUARTER.legacyName)
        assertEquals("Waning_Crescent", MoonPhase.WANING_CRESCENT.legacyName)
    }

    private fun phaseAt(fraction: Double): MoonPhase {
        val millis = MoonPhase.EPOCH_MILLIS + (MoonPhase.SYNODIC_MONTH_MILLIS * fraction).toLong()
        return MoonPhase.fromMillis(millis)
    }
}
