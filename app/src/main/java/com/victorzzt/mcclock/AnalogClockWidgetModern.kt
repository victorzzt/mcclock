package com.victorzzt.mcclock

class AnalogClockWidgetModern : BaseClockWidgetProvider(
    layoutResId = R.layout.widget_analog_clock_modern,
    renderer = SpriteSheetClockRenderer(
        spriteSheetResId = R.drawable.clock_17
    )
)
