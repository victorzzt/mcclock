package com.victorzzt.mcclock

import org.junit.Assert.assertEquals
import org.junit.Test

class ClockTimeTest {
    @Test
    fun totalSecondsUsesTwentyFourHourClock() {
        assertEquals(0, ClockTime(0, 0, 0).totalSeconds)
        assertEquals(43_200, ClockTime(12, 0, 0).totalSeconds)
        assertEquals(86_399, ClockTime(23, 59, 59).totalSeconds)
    }

    @Test
    fun dialAngleKeepsExistingHalfTurnOffset() {
        assertEquals(Math.PI.toFloat(), ClockTime(0, 0, 0).dialAngleRadians(), EPSILON)
        assertEquals(0f, ClockTime(12, 0, 0).dialAngleRadians(), EPSILON)
    }

    @Test
    fun spriteFrameIndexMapsMidnightToFrameThirtyTwo() {
        assertEquals(32, ClockTime(0, 0, 0).spriteFrameIndex())
        assertEquals(0, ClockTime(12, 0, 0).spriteFrameIndex())
        assertEquals(31, ClockTime(23, 59, 59).spriteFrameIndex())
    }

    private companion object {
        const val EPSILON = 0.000001f
    }
}
