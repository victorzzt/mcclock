package com.victorzzt.mcclock

import org.junit.Assert.assertEquals
import org.junit.Test

class WidgetVariantTest {
    @Test
    fun variantsKeepTheirLayoutsAndRenderConfigurations() {
        assertEquals(8, WidgetVariant.entries.size)
        WidgetVariant.entries.forEach { variant ->
            assertEquals(R.layout.widget_clock, variant.layoutResId)
        }

        assertVariant(
            WidgetVariant.CLASSIC,
            ClockRenderConfig.Procedural(
                R.drawable.clock_mask_classic,
                R.drawable.clock_dial_classic
            )
        )
        assertVariant(
            WidgetVariant.CLASSIC_HI_RES,
            ClockRenderConfig.Procedural(
                R.drawable.clock_mask_classic,
                R.drawable.clock_dial_classic,
                HIGH_RES_SIZE_PX
            )
        )
        assertVariant(
            WidgetVariant.CLASSIC_MOON_PHASE,
            ClockRenderConfig.MoonPhase(
                R.drawable.clock_mask_classic,
                MoonPhaseDialResources.classic
            )
        )
        assertVariant(
            WidgetVariant.CLASSIC_15,
            ClockRenderConfig.SpriteSheet(R.drawable.clock_15)
        )
        assertVariant(
            WidgetVariant.MODERN,
            ClockRenderConfig.SpriteSheet(R.drawable.clock_17)
        )
        assertVariant(
            WidgetVariant.MODERN_RENDERED,
            ClockRenderConfig.Procedural(
                R.drawable.clock_mask_modern,
                R.drawable.clock_dial_modern
            )
        )
        assertVariant(
            WidgetVariant.MODERN_HI_RES,
            ClockRenderConfig.Procedural(
                R.drawable.clock_mask_modern,
                R.drawable.clock_dial_modern,
                HIGH_RES_SIZE_PX
            )
        )
        assertVariant(
            WidgetVariant.MODERN_MOON_PHASE,
            ClockRenderConfig.MoonPhase(
                R.drawable.clock_mask_modern,
                MoonPhaseDialResources.modern
            )
        )
    }

    private fun assertVariant(
        variant: WidgetVariant,
        expectedRenderConfig: ClockRenderConfig
    ) {
        assertEquals(expectedRenderConfig, variant.renderConfig)
    }
}
