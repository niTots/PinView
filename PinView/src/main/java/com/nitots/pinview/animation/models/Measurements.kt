package com.nitots.pinview.animation.models

data class Measurements(
    val measuredPinViewWidth: Float,
    val partCount: Int,
    val cornerSize: Float,
    val borderSize: Float
) {
    val distanceToTheBorder: Float
        get() = (measuredPinViewWidth - pinWidth) / 2

    val partWidth: Float
        get() = measuredPinViewWidth / partCount

    val pinWidth: Float
        get() = measuredPinViewWidth - (partWidth * 3)
}