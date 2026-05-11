package com.victorzzt.mcclock

import android.appwidget.AppWidgetProvider
import android.appwidget.AppWidgetManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import java.util.Calendar

/**
 * Analog Clock Widget variant based on Minecraft 1.15+ sprite-based rendering.
 * Instead of procedural rotation, it selects a specific 16x16 frame from a
 * 16x1024 sprite sheet containing 64 possible time states.
 */
class AnalogClockWidgetClassic_15 : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        // Total seconds elapsed in the day
        val totalSeconds = hour * 3600 + minute * 60 + second
        val secondsInDay = 86400

        // Map the day to 64 frames (0-63)
        // Minecraft clock time starts at 6000 (noon). 0 is 6:00 AM.
        // For simplicity, we just divide the day into 64 slices.
        // If we want to align with MC behavior where 0 is dawn:
        // var frameIndex = ((totalSeconds.toFloat() / secondsInDay) * 64).toInt() % 64
        
        // However, the sprite sheet usually starts at noon or dawn depending on the version.
        // Standard MC 1.15+ clock_15.png: Frame 0 is noon, 32 is midnight.
        // We'll align 0.0 progression with Frame 0.
        val progress = totalSeconds.toFloat() / secondsInDay
        val frameIndex = ((progress * 64).toInt() + 32) % 64

        val options = BitmapFactory.Options().apply {
            inScaled = false
        }

        val spriteSheet = BitmapFactory.decodeResource(context.resources, R.drawable.clock_15, options)
        
        if (spriteSheet == null) return

        // Extract the 16x16 frame
        // clock_15.png is 16x1024 (64 frames of 16x16)
        val frameWidth = 16
        val frameHeight = 16
        val yOffset = frameIndex * frameHeight
        
        val resultBitmap = Bitmap.createBitmap(spriteSheet, 0, yOffset, frameWidth, frameHeight)

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_analog_clock_classic_15)

            // Fetch widget size
            val optionsBundle = appWidgetManager.getAppWidgetOptions(appWidgetId)
            val minWidthDp = optionsBundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
            val minHeightDp = optionsBundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)

            // Scale image
            val density = context.resources.displayMetrics.density
            val widthPx = (minWidthDp * density).toInt()
            val heightPx = (minHeightDp * density).toInt()

            val scaledBitmap = ClockUtils.scaleKeepAspect(resultBitmap, widthPx, heightPx)
            views.setImageViewBitmap(R.id.clock_image, scaledBitmap)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
