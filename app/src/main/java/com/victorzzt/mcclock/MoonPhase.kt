package com.victorzzt.mcclock

/**
 * 将朔望月连续进度离散为八个常用月相。
 *
 * [legacyName] 保留旧资源命名接口使用的名称，[resourceSuffix] 提供规范化的小写资源后缀。
 * 本枚举描述近似视觉阶段，不用于天文观测或历法计算。
 */
enum class MoonPhase(
    val legacyName: String,
    val resourceSuffix: String
) {
    NEW_MOON("New_Moon", "new_moon"),
    WAXING_CRESCENT("Waxing_Crescent", "waxing_crescent"),
    FIRST_QUARTER("First_Quarter", "first_quarter"),
    WAXING_GIBBOUS("Waxing_Gibbous", "waxing_gibbous"),
    FULL_MOON("Full_Moon", "full_moon"),
    WANING_GIBBOUS("Waning_Gibbous", "waning_gibbous"),
    LAST_QUARTER("Last_Quarter", "last_quarter"),
    WANING_CRESCENT("Waning_Crescent", "waning_crescent");

    companion object {
        /** 已知新月的 UTC 时间戳，用作朔望月周期计算的固定参考点。 */
        const val EPOCH_MILLIS = 947182440000L

        internal const val SYNODIC_MONTH_DAYS = 29.53058867
        private const val MILLIS_PER_DAY = 24 * 60 * 60 * 1000.0
        internal const val SYNODIC_MONTH_MILLIS = SYNODIC_MONTH_DAYS * MILLIS_PER_DAY

        /** 根据当前系统时间返回近似月相。 */
        fun current(): MoonPhase = fromMillis(System.currentTimeMillis())

        /**
         * 根据 UTC 毫秒时间戳返回八等分后的近似月相。
         * 新月跨越周期的零点，因此同时覆盖进度区间的头尾两端。
         */
        fun fromMillis(nowMillis: Long): MoonPhase {
            val phase = phaseFraction(nowMillis)

            return when {
                phase < 0.0625 || phase > 0.9375 -> NEW_MOON
                phase < 0.1875 -> WAXING_CRESCENT
                phase < 0.3125 -> FIRST_QUARTER
                phase < 0.4375 -> WAXING_GIBBOUS
                phase < 0.5625 -> FULL_MOON
                phase < 0.6875 -> WANING_GIBBOUS
                phase < 0.8125 -> LAST_QUARTER
                else -> WANING_CRESCENT
            }
        }

        /**
         * 计算相对于参考新月的归一化朔望月进度，结果位于 `[0, 1)`。
         * 对参考点之前的时间额外归一化，以避开 Kotlin 余数运算产生的负值。
         */
        internal fun phaseFraction(nowMillis: Long): Double {
            val diffMillis = nowMillis - EPOCH_MILLIS
            var phase = (diffMillis % SYNODIC_MONTH_MILLIS) / SYNODIC_MONTH_MILLIS
            if (phase < 0) phase += 1.0
            return phase
        }
    }
}
