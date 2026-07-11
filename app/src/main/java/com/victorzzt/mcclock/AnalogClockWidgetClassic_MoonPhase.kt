package com.victorzzt.mcclock

class AnalogClockWidgetClassic_MoonPhase : BaseClockWidgetProvider(
    layoutResId = R.layout.widget_analog_clock_classic_moonphase,
    renderer = MoonPhaseClockRenderer(
        maskResId = R.drawable.clock_mask_classic,
        dialResources = MoonPhaseDialResources.classic
    )
)
