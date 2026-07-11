package com.victorzzt.mcclock

class AnalogClockWidgetClassic : BaseClockWidgetProvider(
    layoutResId = R.layout.widget_analog_clock_classic,
    renderer = ProceduralClockRenderer(
        maskResId = R.drawable.clock_mask_classic,
        dialResId = R.drawable.clock_dial_classic
    )
)
