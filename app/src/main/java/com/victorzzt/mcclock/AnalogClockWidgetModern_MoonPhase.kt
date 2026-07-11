package com.victorzzt.mcclock

class AnalogClockWidgetModern_MoonPhase : BaseClockWidgetProvider(
    layoutResId = R.layout.widget_analog_clock_modern_moonphase,
    renderer = MoonPhaseClockRenderer(
        maskResId = R.drawable.clock_mask_modern,
        dialResources = MoonPhaseDialResources.modern
    )
)
