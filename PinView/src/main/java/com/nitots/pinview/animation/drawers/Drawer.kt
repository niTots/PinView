package com.nitots.pinview.animation.drawers

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import com.nitots.pinview.animation.models.Measurements

interface Drawer {
    val measurements: Measurements
    val pinColor: Int
    val paint: Paint

    val commonPosition: Float
    val startBound: Float
    val endBound: Float

    fun move(updatedPosition: Float)

    fun color(@ColorInt color: Int, alpha: Int = 255) {
        paint.color = color
        paint.alpha = alpha
    }

    fun draw(canvas: Canvas)
}