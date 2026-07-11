package com.victorzzt.mcclock

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache

/** 为渲染器提供可替换的位图资源加载边界，便于缓存复用和单元测试注入。 */
interface BitmapResourceLoader {
    /**
     * 解码 [resourceId]，并在指定 [targetSizePx] 时将其等比缩放到该正方形边界内。
     * 资源无法解码时返回 `null`，由上层决定回退或跳过本次刷新。
     */
    fun load(context: Context, resourceId: Int, targetSizePx: Int? = null): Bitmap?
}

/**
 * 保存原始资源和预缩放资源的进程内 LRU 缓存。
 *
 * 缓存键包含 [android.content.res.Resources] 实例、配置、资源 ID 和目标尺寸，因此主题、
 * 密度或语言等配置变化不会复用旧位图。缓存总量按位图实际内存限制为 4 MiB，并以同步锁
 * 保护并发访问。进程死亡后对象自然清空，下一次 [load] 会重新建立内容。
 */
object CachedBitmapResourceLoader : BitmapResourceLoader {
    private const val MAX_CACHE_SIZE_KIB = 4 * 1024

    private val lock = Any()
    private val cache = object : LruCache<CacheKey, Bitmap>(MAX_CACHE_SIZE_KIB) {
        override fun sizeOf(key: CacheKey, value: Bitmap): Int =
            (value.allocationByteCount / 1024).coerceAtLeast(1)
    }

    override fun load(context: Context, resourceId: Int, targetSizePx: Int?): Bitmap? {
        val resources = context.resources
        val key = CacheKey(
            resourcesIdentity = System.identityHashCode(resources),
            configurationHash = resources.configuration.hashCode(),
            resourceId = resourceId,
            targetSizePx = targetSizePx
        )

        cachedBitmap(key)?.let { return it }

        val loaded = if (targetSizePx == null) {
            BitmapFactory.decodeResource(resources, resourceId, bitmapOptions())
        } else {
            load(context, resourceId)?.let { source ->
                ClockUtils.scaleKeepAspect(source, targetSizePx, targetSizePx)
            }
        } ?: return null

        synchronized(lock) {
            val current = cache.get(key)
            if (current != null && !current.isRecycled) {
                return current
            }
            cache.put(key, loaded)
        }
        return loaded
    }

    /** 清空所有进程内缓存条目，主要供测试或显式回收场景使用。 */
    fun clear() {
        synchronized(lock) {
            cache.evictAll()
        }
    }

    private fun cachedBitmap(key: CacheKey): Bitmap? = synchronized(lock) {
        val bitmap = cache.get(key) ?: return@synchronized null
        if (bitmap.isRecycled) {
            cache.remove(key)
            null
        } else {
            bitmap
        }
    }

    private data class CacheKey(
        val resourcesIdentity: Int,
        val configurationHash: Int,
        val resourceId: Int,
        val targetSizePx: Int?
    )
}

/** 禁止 Android 按设备密度隐式缩放，使程序化渲染始终使用资源的原始像素网格。 */
private fun bitmapOptions(): BitmapFactory.Options = BitmapFactory.Options().apply {
    inScaled = false
}
