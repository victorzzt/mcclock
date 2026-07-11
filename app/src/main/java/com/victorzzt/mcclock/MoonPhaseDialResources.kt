package com.victorzzt.mcclock

data class MoonPhaseDialResources(
    val fallback: Int,
    val newMoon: Int,
    val waxingCrescent: Int,
    val firstQuarter: Int,
    val waxingGibbous: Int,
    val fullMoon: Int,
    val waningGibbous: Int,
    val lastQuarter: Int,
    val waningCrescent: Int
) {
    fun dialFor(phase: MoonPhase): Int = when (phase) {
        MoonPhase.NEW_MOON -> newMoon
        MoonPhase.WAXING_CRESCENT -> waxingCrescent
        MoonPhase.FIRST_QUARTER -> firstQuarter
        MoonPhase.WAXING_GIBBOUS -> waxingGibbous
        MoonPhase.FULL_MOON -> fullMoon
        MoonPhase.WANING_GIBBOUS -> waningGibbous
        MoonPhase.LAST_QUARTER -> lastQuarter
        MoonPhase.WANING_CRESCENT -> waningCrescent
    }

    companion object {
        val classic = MoonPhaseDialResources(
            fallback = R.drawable.clock_dial_classic,
            newMoon = R.drawable.clock_dial_classic_new_moon,
            waxingCrescent = R.drawable.clock_dial_classic_waxing_crescent,
            firstQuarter = R.drawable.clock_dial_classic_first_quarter,
            waxingGibbous = R.drawable.clock_dial_classic_waxing_gibbous,
            fullMoon = R.drawable.clock_dial_classic_full_moon,
            waningGibbous = R.drawable.clock_dial_classic_waning_gibbous,
            lastQuarter = R.drawable.clock_dial_classic_last_quarter,
            waningCrescent = R.drawable.clock_dial_classic_waning_crescent
        )

        val modern = MoonPhaseDialResources(
            fallback = R.drawable.clock_dial_modern,
            newMoon = R.drawable.clock_dial_modern_new_moon,
            waxingCrescent = R.drawable.clock_dial_modern_waxing_crescent,
            firstQuarter = R.drawable.clock_dial_modern_first_quarter,
            waxingGibbous = R.drawable.clock_dial_modern_waxing_gibbous,
            fullMoon = R.drawable.clock_dial_modern_full_moon,
            waningGibbous = R.drawable.clock_dial_modern_waning_gibbous,
            lastQuarter = R.drawable.clock_dial_modern_last_quarter,
            waningCrescent = R.drawable.clock_dial_modern_waning_crescent
        )
    }
}
