package com.victorzzt.mcclock

class AnalogClockWidgetModern_Rendered : BaseClockWidgetProvider(
    layoutResId = R.layout.widget_analog_clock_modern_rendered,
    renderer = ProceduralClockRenderer(
        maskResId = R.drawable.clock_mask_modern,
        dialResId = R.drawable.clock_dial_modern
    )
)
