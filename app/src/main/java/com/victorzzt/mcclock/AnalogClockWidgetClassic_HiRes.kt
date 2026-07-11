package com.victorzzt.mcclock

class AnalogClockWidgetClassic_HiRes : BaseClockWidgetProvider(
    layoutResId = R.layout.widget_analog_clock_classic_hires,
    renderer = ProceduralClockRenderer(
        maskResId = R.drawable.clock_mask_classic,
        dialResId = R.drawable.clock_dial_classic,
        upscaleSizePx = HIGH_RES_SIZE_PX
    )
)
