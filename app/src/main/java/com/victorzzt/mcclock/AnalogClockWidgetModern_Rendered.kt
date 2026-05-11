package com.victorzzt.mcclock

import android.appwidget.AppWidgetProvider
import android.appwidget.AppWidgetManager
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.RemoteViews

import java.util.Calendar

/**
 * Modern rendered clock widget using procedural rotation with modern assets.
 */
class AnalogClockWidgetModern_Rendered : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)   // 0–23
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        // Total seconds elapsed in the day
        val totalSeconds = hour * 3600 + minute * 60 + second
        val secondsInDay = 86400

        val tau = (2f * Math.PI).toFloat()
        // Convert to angle (radians)
        var angleRad = (totalSeconds.toFloat() / secondsInDay) * (2f * Math.PI.toFloat())
        angleRad += Math.PI.toFloat()
        angleRad = (angleRad % tau + tau) % tau

        val options = BitmapFactory.Options().apply {
            inScaled = false
        }

        val item = BitmapFactory.decodeResource(context.resources, R.drawable.clock_mask_modern, options)
        val dial = BitmapFactory.decodeResource(context.resources, R.drawable.clock_dial_modern, options)

        // Render the base sprite once for all widgets in this update
        val resultBitmap = ClockUtils.setupClockSprite(item, dial, angleRad)

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_analog_clock_modern_rendered)

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
