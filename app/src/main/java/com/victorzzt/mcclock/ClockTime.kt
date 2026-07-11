package com.victorzzt.mcclock

import java.util.Calendar

data class ClockTime(
    val hour24: Int,
    val minute: Int,
    val second: Int
) {
    init {
        require(hour24 in 0..23) { "hour24 must be in 0..23" }
        require(minute in 0..59) { "minute must be in 0..59" }
        require(second in 0..59) { "second must be in 0..59" }
    }

    val totalSeconds: Int = hour24 * SECONDS_PER_HOUR + minute * SECONDS_PER_MINUTE + second

    val dayProgress: Float
        get() = totalSeconds.toFloat() / SECONDS_PER_DAY

    fun dialAngleRadians(): Float {
        var angleRad = dayProgress * TAU
        angleRad += Math.PI.toFloat()
        return (angleRad % TAU + TAU) % TAU
    }

    fun spriteFrameIndex(
        frameCount: Int = DEFAULT_SPRITE_FRAME_COUNT,
        midnightFrameOffset: Int = DEFAULT_MIDNIGHT_FRAME_OFFSET
    ): Int = ((dayProgress * frameCount).toInt() + midnightFrameOffset) % frameCount

    companion object {
        const val SECONDS_PER_MINUTE = 60
        const val SECONDS_PER_HOUR = 60 * SECONDS_PER_MINUTE
        const val SECONDS_PER_DAY = 24 * SECONDS_PER_HOUR
        const val DEFAULT_SPRITE_FRAME_COUNT = 64
        const val DEFAULT_MIDNIGHT_FRAME_OFFSET = 32

        private val TAU = 2f * Math.PI.toFloat()

        fun now(): ClockTime = from(Calendar.getInstance())

        fun from(calendar: Calendar): ClockTime = ClockTime(
            hour24 = calendar.get(Calendar.HOUR_OF_DAY),
            minute = calendar.get(Calendar.MINUTE),
            second = calendar.get(Calendar.SECOND)
        )
    }
}
