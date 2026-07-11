package com.victorzzt.mcclock

import java.util.Calendar

/**
 * 与日期和时区规则解耦的一天内时间快照。
 *
 * 构造时会校验 24 小时制字段范围；后续计算以午夜为零点，把一天线性映射到表盘角度或
 * 精灵表帧序。日期采集仅发生在 [now] 和 [from]，因此渲染计算可以确定性测试。
 */
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

    /** 从本地午夜起累计的完整秒数。 */
    val totalSeconds: Int = hour24 * SECONDS_PER_HOUR + minute * SECONDS_PER_MINUTE + second

    /** 当天已经经过的比例，取值为 `[0, 1)`。 */
    val dayProgress: Float
        get() = totalSeconds.toFloat() / SECONDS_PER_DAY

    /**
     * 返回程序化表盘使用的弧度角。
     *
     * 半圈偏移使午夜对齐素材中代表夜晚的朝向，归一化后结果始终位于 `[0, 2π)`。
     */
    fun dialAngleRadians(): Float {
        var angleRad = dayProgress * TAU
        angleRad += Math.PI.toFloat()
        return (angleRad % TAU + TAU) % TAU
    }

    /** 将当天进度离散到精灵表帧，并应用午夜帧偏移。 */
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

        /** 从当前系统日历读取本地时间快照。 */
        fun now(): ClockTime = from(Calendar.getInstance())

        /** 从给定日历复制时、分、秒字段，不保留对可变 [Calendar] 的引用。 */
        fun from(calendar: Calendar): ClockTime = ClockTime(
            hour24 = calendar.get(Calendar.HOUR_OF_DAY),
            minute = calendar.get(Calendar.MINUTE),
            second = calendar.get(Calendar.SECOND)
        )
    }
}
