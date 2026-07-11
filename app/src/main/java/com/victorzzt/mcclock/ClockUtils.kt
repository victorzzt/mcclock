package com.victorzzt.mcclock

import android.graphics.Bitmap
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/** 不依赖 Widget 生命周期的底层位图合成与缩放操作。 */
object ClockUtils {

    /**
     * 按 [dialAngle] 旋转表盘纹理，并将其填入 [item] 遮罩中以合成时钟帧。
     *
     * 遮罩里的非零洋红色灰阶像素是可替换区域：其红色通道既充当亮度，也用于调制表盘
     * 颜色；其他像素原样复制。像素批量读写可减少 JNI 往返，[outBitmap] 则允许调用方在
     * 尺寸和可变性满足条件时复用输出内存。
     *
     * 算法参考：https://minecraft.wiki/w/Procedural_animated_texture_generation/Clocks
     *
     * @param item 包含固定外观和可替换区域的遮罩位图。
     * @param dial 可平铺采样的表盘纹理。
     * @param dialAngle 表盘旋转角度，单位为弧度。
     * @param outBitmap 可选的可变输出位图；尺寸不匹配时会忽略并新建位图。
     * @return 合成后的时钟帧。
     */
    fun setupClockSprite(
        item: Bitmap,
        dial: Bitmap,
        dialAngle: Float,
        outBitmap: Bitmap? = null
    ): Bitmap {
        val width = item.width
        val height = item.height

        val output = if (outBitmap != null && outBitmap.isMutable &&
            outBitmap.width == width && outBitmap.height == height
        ) {
            outBitmap
        } else {
            Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }

        val rx = sin(-dialAngle)
        val ry = cos(-dialAngle)

        val itemPixels = IntArray(width * height)
        val outputPixels = IntArray(width * height)
        item.getPixels(itemPixels, 0, width, 0, 0, width, height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val index = y * width + x
                val pix = itemPixels[index]

                val r = (pix shr 16 and 0xFF) / 255f
                val g = (pix shr 8 and 0xFF) / 255f
                val b = (pix and 0xFF) / 255f

                if (r == b && g == 0f && b > 0f) {
                    val u = -(x.toFloat() / (width - 1) - 0.5f)
                    val v = (y.toFloat() / (height - 1) - 0.5f)

                    var dx = ((u * ry + v * rx + 0.5f) * dial.width).roundToInt() % dial.width
                    var dy = ((v * ry - u * rx + 0.5f) * dial.height).roundToInt() % dial.height

                    if (dx < 0) dx += dial.width
                    if (dy < 0) dy += dial.height

                    val dialPix = dial.getPixel(dx, dy)

                    val dr = ((dialPix shr 16 and 0xFF) * r).roundToInt().coerceIn(0, 255)
                    val dg = ((dialPix shr 8 and 0xFF) * r).roundToInt().coerceIn(0, 255)
                    val db = ((dialPix and 0xFF) * r).roundToInt().coerceIn(0, 255)

                    outputPixels[index] = (0xFF shl 24) or (dr shl 16) or (dg shl 8) or db
                } else {
                    outputPixels[index] = pix
                }
            }
        }

        output.setPixels(outputPixels, 0, width, 0, 0, width, height)
        return output
    }

    /**
     * 在不裁剪、不拉伸的前提下，把位图等比缩放到 [maxW] × [maxH] 边界内。
     *
     * 使用最近邻缩放以保留像素画边缘；调用方应传入正的目标尺寸。
     */
    fun scaleKeepAspect(src: Bitmap, maxW: Int, maxH: Int): Bitmap {
        val ratio = minOf(
            maxW.toFloat() / src.width,
            maxH.toFloat() / src.height
        )

        val newW = (src.width * ratio).toInt()
        val newH = (src.height * ratio).toInt()

        return Bitmap.createScaledBitmap(src, newW, newH, false)
    }

    /**
     * 返回旧版调用方使用的月相资源后缀名称。
     *
     * 新代码应优先直接使用 [MoonPhase]；此方法仅维持历史接口兼容性。
     */
    fun calculateLunarPhase(): String = MoonPhase.current().legacyName
}
