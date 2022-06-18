package com.nitots.pinview.animation.drawers

import android.graphics.Canvas
import android.graphics.Paint
import com.nitots.pinview.animation.models.Measurements

open class SquareDrawer(
    override val measurements: Measurements,
    override val pinColor: Int,
) : Drawer {

    override val paint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        color = pinColor
    }
    /*
    *
    * First things first: distanceToTheBorder = (whole PinView width - pinWidth) / 2
    * Because of the square drawing, we have 2 agile parts: start border and end border.
    * Having this in mind will reduce the time of thinking on movement logic.
    * */

    override val commonPosition: Float = measurements.distanceToTheBorder
    override val startBound: Float = 0f + measurements.borderSize
    override val endBound: Float = (measurements.distanceToTheBorder * 2) - measurements.borderSize

    private var updatableStart: Float = measurements.distanceToTheBorder
    private var updatableEnd: Float =
        measurements.measuredPinViewWidth - measurements.distanceToTheBorder

    override fun move(updatedPosition: Float) {
        updatableStart = updatedPosition
        updatableEnd =
            measurements.measuredPinViewWidth - (measurements.distanceToTheBorder + (measurements.distanceToTheBorder - updatedPosition))
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRoundRect(
            updatableStart,
            measurements.distanceToTheBorder,
            updatableEnd,
            measurements.measuredPinViewWidth - measurements.distanceToTheBorder,
            measurements.cornerSize,
            measurements.cornerSize,
            paint
        )
    }
}