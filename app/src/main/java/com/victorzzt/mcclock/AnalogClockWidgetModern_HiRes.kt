package com.victorzzt.mcclock

class AnalogClockWidgetModern_HiRes : BaseClockWidgetProvider(
    layoutResId = R.layout.widget_analog_clock_modern_hires,
    renderer = ProceduralClockRenderer(
        maskResId = R.drawable.clock_mask_modern,
        dialResId = R.drawable.clock_dial_modern,
        upscaleSizePx = HIGH_RES_SIZE_PX
    )
)
