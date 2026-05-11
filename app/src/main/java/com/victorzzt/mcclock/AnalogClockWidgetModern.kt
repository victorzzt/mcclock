package com.victorzzt.mcclock

import android.appwidget.AppWidgetProvider
import android.appwidget.AppWidgetManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import java.util.Calendar

/**
 * Analog Clock Widget variant based on Minecraft 1.17+ sprite-based rendering.
 * Uses 16x16 frames from clock_17.png (64 frames).
 */
class AnalogClockWidgetModern : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        val totalSeconds = hour * 3600 + minute * 60 + second
        val secondsInDay = 86400

        val progress = totalSeconds.toFloat() / secondsInDay
        // Frame 0 is noon, 32 is midnight. +32 offset maps midnight (0.0) to 32.
        val frameIndex = ((progress * 64).toInt() + 32) % 64

        val options = BitmapFactory.Options().apply {
            inScaled = false
        }

        val spriteSheet = BitmapFactory.decodeResource(context.resources, R.drawable.clock_17, options)
        
        if (spriteSheet == null) return

        val frameWidth = 16
        val frameHeight = 16
        val yOffset = frameIndex * frameHeight
        
        val resultBitmap = Bitmap.createBitmap(spriteSheet, 0, yOffset, frameWidth, frameHeight)

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_analog_clock_modern)

            val optionsBundle = appWidgetManager.getAppWidgetOptions(appWidgetId)
            val minWidthDp = optionsBundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
            val minHeightDp = optionsBundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)

            val density = context.resources.displayMetrics.density
            val widthPx = (minWidthDp * density).toInt()
            val heightPx = (minHeightDp * density).toInt()

            val scaledBitmap = ClockUtils.scaleKeepAspect(resultBitmap, widthPx, heightPx)
            views.setImageViewBitmap(R.id.clock_image, scaledBitmap)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
