package com.victorzzt.mcclock

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

/**
 * 八种时钟组件共用的 AppWidget 生命周期与绘制入口。
 *
 * 子类只负责绑定一个 [WidgetVariant]；本类在组件活跃期间安排分钟刷新，并在每次更新时
 * 复用该变体的渲染器。最终位图会按每个组件实例的当前尺寸分别缩放后写入 [RemoteViews]。
 */
abstract class BaseClockWidgetProvider(
    private val variant: WidgetVariant
) : AppWidgetProvider() {
    private val renderer by lazy(LazyThreadSafetyMode.NONE) {
        variant.createRenderer()
    }

    /** 响应系统更新，确保主动刷新任务存在，并立即重绘系统给出的组件实例。 */
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        WidgetRefreshScheduler.schedule(context, javaClass)
        renderWidgets(context, appWidgetManager, appWidgetIds)
    }

    /**
     * 仅接管应用自身的刷新广播，其余广播仍交由 [AppWidgetProvider] 处理。
     *
     * 组件 ID 在回调时重新查询，避免定时任务使用已删除实例的过期 ID；若该 Provider 已无
     * 实例，则顺便撤销对应闹钟。
     */
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != WidgetRefreshScheduler.ACTION_REFRESH) {
            super.onReceive(context, intent)
            return
        }

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, javaClass))
        if (appWidgetIds.isEmpty()) {
            WidgetRefreshScheduler.cancel(context, javaClass)
            return
        }
        renderWidgets(context, appWidgetManager, appWidgetIds)
    }

    /** 第一个此类组件加入桌面时启动该 Provider 专属的主动刷新任务。 */
    override fun onEnabled(context: Context) {
        WidgetRefreshScheduler.schedule(context, javaClass)
    }

    /** 最后一个此类组件移除后撤销该 Provider 专属的主动刷新任务。 */
    override fun onDisabled(context: Context) {
        WidgetRefreshScheduler.cancel(context, javaClass)
    }

    /**
     * 为同一批实例只渲染一次当前时刻，再依据各实例的启动器尺寸生成适配位图。
     * 渲染资源缺失时保持桌面现有内容，避免用空图覆盖仍可用的上一次结果。
     */
    private fun renderWidgets(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val resultBitmap = renderer.render(context, ClockTime.now()) ?: return

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, variant.layoutResId)
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
