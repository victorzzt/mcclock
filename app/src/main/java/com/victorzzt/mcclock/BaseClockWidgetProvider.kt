package com.victorzzt.mcclock

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

abstract class BaseClockWidgetProvider(
    private val layoutResId: Int,
    private val renderer: ClockRenderer
) : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val resultBitmap = renderer.render(context, ClockTime.now()) ?: return

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, layoutResId)
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
