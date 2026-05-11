package com.victorzzt.mcclock

import android.graphics.Bitmap
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Utility class for clock widget operations.
 */
object ClockUtils {

    /**
     * Renders the clock sprite by applying a dial texture onto a mask based on the provided angle.
     * Uses batch pixel operations to minimize JNI overhead and supports reusable bitmaps.
     * Adapted from: https://minecraft.wiki/w/Procedural_animated_texture_generation/Clocks
     *
     * @param item The mask bitmap.
     * @param dial The dial texture bitmap.
     * @param dialAngle The angle of the dial in radians.
     * @param outBitmap An optional mutable bitmap to reuse for output. Must match item dimensions.
     * @return The rendered clock sprite bitmap.
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
     * Creates a scaled version of the bitmap while maintaining the aspect ratio.
     *
     * @param src The source bitmap.
     * @param maxW The maximum width allowed.
     * @param maxH The maximum height allowed.
     * @return A scaled Bitmap.
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
     * Calculates the current lunar phase as a descriptive string.
     * Uses a linear approximation based on the average synodic month (29.53059 days)
     * and a reference New Moon epoch (January 6, 2000, 18:14 UTC).
     *
     * @return A string representing the current lunar phase (e.g., "Full Moon").
     */
    fun calculateLunarPhase(): String {
        // Reference New Moon: Jan 6, 2000, 18:14 UTC
        val epochMillis = 947182440000L
        val nowMillis = System.currentTimeMillis()
        
        // Mean synodic month in milliseconds
        val synodicMonthMillis = 29.53058867 * 24 * 60 * 60 * 1000
        
        val diffMillis = nowMillis - epochMillis
        var phase = (diffMillis % synodicMonthMillis) / synodicMonthMillis
        
        // Handle negative phase for dates before epoch
        if (phase < 0) phase += 1.0

        return when {
            phase < 0.0625 || phase > 0.9375 -> "New_Moon"
            phase < 0.1875 -> "Waxing_Crescent"
            phase < 0.3125 -> "First_Quarter"
            phase < 0.4375 -> "Waxing_Gibbous"
            phase < 0.5625 -> "Full_Moon"
            phase < 0.6875 -> "Waning_Gibbous"
            phase < 0.8125 -> "Last_Quarter"
            else -> "Waning_Crescent"
        }
    }
}
