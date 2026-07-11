package com.victorzzt.mcclock

/**
 * 一个视觉家族的完整月相表盘资源表。
 *
 * [fallback] 是月相专用资源解码失败时使用的普通表盘；其余字段与 [MoonPhase] 一一对应，
 * 让渲染器无需根据文件名或反射动态查找资源。
 */
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
    /** 返回 [phase] 对应的编译期 drawable 资源 ID。 */
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
        /** 经典材质家族的月相表盘映射。 */
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

        /** 现代材质家族的月相表盘映射。 */
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
