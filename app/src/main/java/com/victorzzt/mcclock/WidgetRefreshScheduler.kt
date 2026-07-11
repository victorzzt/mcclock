package com.victorzzt.mcclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock

/**
 * 在设备唤醒期间为每个 Provider 安排尽力而为的分钟刷新。
 *
 * 使用非唤醒、非精确的重复闹钟，让 Android 可以合并任务，并允许设备休眠、Doze 或厂商
 * 省电策略延迟刷新。Provider XML 中的 30 分钟周期仍作为系统级兜底。
 */
internal object WidgetRefreshScheduler {
    /** 仅发往目标 Provider 类的应用内组件刷新动作。 */
    const val ACTION_REFRESH = "com.victorzzt.mcclock.action.REFRESH_WIDGET"

    /**
     * 创建或更新 [providerClass] 对应的重复刷新闹钟。
     * 首次触发会对齐到下一个墙上时钟周期边界，使分钟显示尽量接近整分钟更新。
     */
    fun schedule(context: Context, providerClass: Class<*>) {
        val intervalMillis = context.resources
            .getInteger(R.integer.widget_active_refresh_interval_ms)
            .toLong()
        val triggerAtMillis = nextRefreshTriggerElapsedRealtime(
            nowWallClockMillis = System.currentTimeMillis(),
            nowElapsedRealtimeMillis = SystemClock.elapsedRealtime(),
            intervalMillis = intervalMillis
        )

        context.getSystemService(AlarmManager::class.java).setRepeating(
            AlarmManager.ELAPSED_REALTIME,
            triggerAtMillis,
            intervalMillis,
            refreshIntent(context, providerClass)
        )
    }

    /** 撤销 [providerClass] 对应的刷新闹钟，不影响其他 Widget 变体。 */
    fun cancel(context: Context, providerClass: Class<*>) {
        context.getSystemService(AlarmManager::class.java)
            .cancel(refreshIntent(context, providerClass))
    }

    private fun refreshIntent(context: Context, providerClass: Class<*>): PendingIntent {
        val intent = Intent(context, providerClass).setAction(ACTION_REFRESH)
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}

/**
 * 把下一个墙上时钟周期边界换算为 [android.os.SystemClock.elapsedRealtime] 时间基准。
 *
 * 当调用恰好落在边界上时选择下一个完整周期，避免创建立即触发并重置节拍的闹钟。
 */
internal fun nextRefreshTriggerElapsedRealtime(
    nowWallClockMillis: Long,
    nowElapsedRealtimeMillis: Long,
    intervalMillis: Long
): Long {
    require(intervalMillis > 0)
    val remainder = nowWallClockMillis % intervalMillis
    val delayMillis = if (remainder == 0L) intervalMillis else intervalMillis - remainder
    return nowElapsedRealtimeMillis + delayMillis
}
