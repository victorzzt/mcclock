package com.victorzzt.mcclock

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
        const val EPOCH_MILLIS = 947182440000L

        internal const val SYNODIC_MONTH_DAYS = 29.53058867
        private const val MILLIS_PER_DAY = 24 * 60 * 60 * 1000.0
        internal const val SYNODIC_MONTH_MILLIS = SYNODIC_MONTH_DAYS * MILLIS_PER_DAY

        fun current(): MoonPhase = fromMillis(System.currentTimeMillis())

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

        internal fun phaseFraction(nowMillis: Long): Double {
            val diffMillis = nowMillis - EPOCH_MILLIS
            var phase = (diffMillis % SYNODIC_MONTH_MILLIS) / SYNODIC_MONTH_MILLIS
            if (phase < 0) phase += 1.0
            return phase
        }
    }
}
