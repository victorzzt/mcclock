package com.victorzzt.mcclock

import android.content.Context
import android.graphics.Bitmap

/** 将一个确定的 [ClockTime] 转换为可交给 AppWidget 显示的时钟位图。 */
interface ClockRenderer {
    /** 生成当前帧；依赖资源不可用时返回 `null`，使 Provider 保留上一次成功画面。 */
    fun render(context: Context, clockTime: ClockTime): Bitmap?
}

/**
 * 通过旋转表盘纹理并套用时钟遮罩来生成一帧程序化时钟。
 *
 * [upscaleSizePx] 只控制输入资源的预缩放；最终桌面尺寸仍由 Provider 统一适配。
 */
class ProceduralClockRenderer(
    private val maskResId: Int,
    private val dialResId: Int,
    private val upscaleSizePx: Int? = null,
    private val bitmapLoader: BitmapResourceLoader = CachedBitmapResourceLoader
) : ClockRenderer {
    override fun render(context: Context, clockTime: ClockTime): Bitmap? {
        val mask = bitmapLoader.load(context, maskResId, upscaleSizePx) ?: return null
        val dial = bitmapLoader.load(context, dialResId, upscaleSizePx) ?: return null

        return ClockUtils.setupClockSprite(mask, dial, clockTime.dialAngleRadians())
    }
}

/**
 * 在程序化渲染基础上，根据现实月相选择表盘纹理。
 *
 * 若指定月相资源解码失败，会尝试 [MoonPhaseDialResources.fallback]；遮罩或回退表盘也不可用
 * 时才放弃本帧。
 */
class MoonPhaseClockRenderer(
    private val maskResId: Int,
    private val dialResources: MoonPhaseDialResources,
    private val upscaleSizePx: Int? = HIGH_RES_SIZE_PX,
    private val moonPhaseProvider: () -> MoonPhase = MoonPhase::current,
    private val bitmapLoader: BitmapResourceLoader = CachedBitmapResourceLoader
) : ClockRenderer {
    override fun render(context: Context, clockTime: ClockTime): Bitmap? {
        val mask = bitmapLoader.load(context, maskResId, upscaleSizePx) ?: return null
        val dialResId = dialResources.dialFor(moonPhaseProvider())
        val dial = bitmapLoader.load(context, dialResId, upscaleSizePx)
            ?: bitmapLoader.load(context, dialResources.fallback, upscaleSizePx)
            ?: return null

        return ClockUtils.setupClockSprite(mask, dial, clockTime.dialAngleRadians())
    }
}

/**
 * 从纵向排列的精灵表中截取与当前一天进度对应的固定尺寸帧。
 *
 * [midnightFrameOffset] 用于把民用时间的午夜对齐到 Minecraft 时钟精灵表的帧序。
 */
class SpriteSheetClockRenderer(
    private val spriteSheetResId: Int,
    private val frameWidth: Int = 16,
    private val frameHeight: Int = 16,
    private val frameCount: Int = ClockTime.DEFAULT_SPRITE_FRAME_COUNT,
    private val midnightFrameOffset: Int = ClockTime.DEFAULT_MIDNIGHT_FRAME_OFFSET,
    private val bitmapLoader: BitmapResourceLoader = CachedBitmapResourceLoader
) : ClockRenderer {
    override fun render(context: Context, clockTime: ClockTime): Bitmap? {
        val spriteSheet = bitmapLoader.load(context, spriteSheetResId) ?: return null
        val frameIndex = clockTime.spriteFrameIndex(frameCount, midnightFrameOffset)
        val yOffset = frameIndex * frameHeight

        return Bitmap.createBitmap(spriteSheet, 0, yOffset, frameWidth, frameHeight)
    }
}

/** 高清程序化变体预先使用的方形资源边长；单位为像素。 */
const val HIGH_RES_SIZE_PX = 256
