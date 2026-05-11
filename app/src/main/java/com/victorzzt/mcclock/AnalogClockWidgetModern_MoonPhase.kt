package com.victorzzt.mcclock

import android.appwidget.AppWidgetProvider
import android.appwidget.AppWidgetManager
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import java.util.Calendar
import java.util.Locale

/**
 * Modern Moon Phase clock widget using procedural rotation with modern assets.
 * Label: "DynMoon"
 */
class AnalogClockWidgetModern_MoonPhase : AppWidgetProvider() {

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

        val tau = (2f * Math.PI).toFloat()
        var angleRad = (totalSeconds.toFloat() / secondsInDay) * (2f * Math.PI.toFloat())
        angleRad += Math.PI.toFloat()
        angleRad = (angleRad % tau + tau) % tau

        val options = BitmapFactory.Options().apply {
            inScaled = false
        }

        val itemRaw = BitmapFactory.decodeResource(context.resources, R.drawable.clock_mask_modern, options)
        
        // Determine lunar phase dial
        val moonPhase = ClockUtils.calculateLunarPhase().lowercase(Locale.US)

        val drawableName = "clock_dial_modern_$moonPhase"
        val dialResId = context.resources.getIdentifier(drawableName, "drawable", context.packageName)

        val dialRaw = if (dialResId != 0) {
            BitmapFactory.decodeResource(context.resources, dialResId, options)
        } else {
            // Fallback to modern dial if specific moon phase asset is missing
            BitmapFactory.decodeResource(context.resources, R.drawable.clock_dial_modern, options)
        }

        // Upscale to 256x256 using non-filtered scaling
        val item = ClockUtils.scaleKeepAspect(itemRaw, 256, 256)
        val dial = ClockUtils.scaleKeepAspect(dialRaw, 256, 256)

        val resultBitmap = ClockUtils.setupClockSprite(item, dial, angleRad)

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_analog_clock_modern_moonphase)

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
