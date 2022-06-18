package com.nitots.pinview.animation.drawers

import android.graphics.Canvas
import android.graphics.Paint
import com.nitots.pinview.animation.models.Measurements

open class CircleDrawer(
    override val measurements: Measurements,
    override val pinColor: Int
) : Drawer {

    override val paint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        color = pinColor
    }

    private val radius = measurements.pinWidth / 2
    private val constantYCoordinate = measurements.measuredPinViewWidth / 2
    private var updatableXCoordinate: Float = constantYCoordinate

    override val commonPosition: Float = measurements.measuredPinViewWidth / 2
    override val startBound: Float = radius + measurements.borderSize
    override val endBound: Float =
        (measurements.measuredPinViewWidth - radius) - measurements.borderSize


    override fun move(updatedPosition: Float) {
        updatableXCoordinate = updatedPosition
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(
            updatableXCoordinate,
            constantYCoordinate,
            radius,
            paint
        )
    }
}