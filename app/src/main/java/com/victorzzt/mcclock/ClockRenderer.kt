package com.victorzzt.mcclock

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

interface ClockRenderer {
    fun render(context: Context, clockTime: ClockTime): Bitmap?
}

class ProceduralClockRenderer(
    private val maskResId: Int,
    private val dialResId: Int,
    private val upscaleSizePx: Int? = null
) : ClockRenderer {
    override fun render(context: Context, clockTime: ClockTime): Bitmap? {
        val options = bitmapOptions()
        val maskRaw = BitmapFactory.decodeResource(context.resources, maskResId, options) ?: return null
        val dialRaw = BitmapFactory.decodeResource(context.resources, dialResId, options) ?: return null

        val mask = upscaleSizePx?.let { ClockUtils.scaleKeepAspect(maskRaw, it, it) } ?: maskRaw
        val dial = upscaleSizePx?.let { ClockUtils.scaleKeepAspect(dialRaw, it, it) } ?: dialRaw

        return ClockUtils.setupClockSprite(mask, dial, clockTime.dialAngleRadians())
    }
}

class MoonPhaseClockRenderer(
    private val maskResId: Int,
    private val dialResources: MoonPhaseDialResources,
    private val upscaleSizePx: Int? = HIGH_RES_SIZE_PX,
    private val moonPhaseProvider: () -> MoonPhase = MoonPhase::current
) : ClockRenderer {
    override fun render(context: Context, clockTime: ClockTime): Bitmap? {
        val options = bitmapOptions()
        val maskRaw = BitmapFactory.decodeResource(context.resources, maskResId, options) ?: return null
        val dialResId = dialResources.dialFor(moonPhaseProvider())
        val dialRaw = BitmapFactory.decodeResource(context.resources, dialResId, options)
            ?: BitmapFactory.decodeResource(context.resources, dialResources.fallback, options)
            ?: return null

        val mask = upscaleSizePx?.let { ClockUtils.scaleKeepAspect(maskRaw, it, it) } ?: maskRaw
        val dial = upscaleSizePx?.let { ClockUtils.scaleKeepAspect(dialRaw, it, it) } ?: dialRaw

        return ClockUtils.setupClockSprite(mask, dial, clockTime.dialAngleRadians())
    }
}

class SpriteSheetClockRenderer(
    private val spriteSheetResId: Int,
    private val frameWidth: Int = 16,
    private val frameHeight: Int = 16,
    private val frameCount: Int = ClockTime.DEFAULT_SPRITE_FRAME_COUNT,
    private val midnightFrameOffset: Int = ClockTime.DEFAULT_MIDNIGHT_FRAME_OFFSET
) : ClockRenderer {
    override fun render(context: Context, clockTime: ClockTime): Bitmap? {
        val spriteSheet = BitmapFactory.decodeResource(context.resources, spriteSheetResId, bitmapOptions())
            ?: return null
        val frameIndex = clockTime.spriteFrameIndex(frameCount, midnightFrameOffset)
        val yOffset = frameIndex * frameHeight

        return Bitmap.createBitmap(spriteSheet, 0, yOffset, frameWidth, frameHeight)
    }
}

private fun bitmapOptions(): BitmapFactory.Options = BitmapFactory.Options().apply {
    inScaled = false
}

const val HIGH_RES_SIZE_PX = 256
