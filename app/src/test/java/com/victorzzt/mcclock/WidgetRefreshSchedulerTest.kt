package com.victorzzt.mcclock

import org.junit.Assert.assertEquals
import org.junit.Test

class WidgetRefreshSchedulerTest {
    @Test
    fun nextTriggerAlignsToTheNextWallClockInterval() {
        assertEquals(
            559_999L,
            nextRefreshTriggerElapsedRealtime(
                nowWallClockMillis = 120_001L,
                nowElapsedRealtimeMillis = 500_000L,
                intervalMillis = 60_000L
            )
        )
    }

    @Test
    fun exactBoundarySchedulesTheFollowingInterval() {
        assertEquals(
            560_000L,
            nextRefreshTriggerElapsedRealtime(
                nowWallClockMillis = 120_000L,
                nowElapsedRealtimeMillis = 500_000L,
                intervalMillis = 60_000L
            )
        )
    }
}
