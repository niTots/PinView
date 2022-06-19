package com.nitots.pinview.helpers

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.util.TypedValue
import androidx.appcompat.content.res.AppCompatResources
import com.nitots.pinview.R
import com.nitots.pinview.animation.AnimationComponentsFactory
import com.nitots.pinview.animation.AnimationLauncher
import com.nitots.pinview.animation.models.ColorPalette
import com.nitots.pinview.animation.models.Measurements
import com.nitots.pinview.views.PinView

internal class PinViewHelper(
    private val pinView: PinView,
    pinTypedArray: TypedArray,
    materialCardTypedArray: TypedArray
) {
    private val pinSize: Size
    private val drawingStyle: DrawerStyle
    private val animationStyle: AnimationStyle
    private val strokeCommonColor: ColorStateList
    private val strokeErrorColor: Int
    private val pinCommonColor: Int
    private val pinErrorColor: Int
    private val animationDuration: Long
    private val palette: ColorPalette

    init {
        pinSize = getPinSize(pinTypedArray)
        drawingStyle = getDrawingStyle(pinTypedArray)
        animationStyle = getAnimationStyle(pinTypedArray)
        strokeCommonColor = getStrokeCommonColor(materialCardTypedArray)
        strokeErrorColor = getStrokeErrorColor()
        pinCommonColor = getPinCommonColor(pinTypedArray)
        pinErrorColor = strokeErrorColor
        animationDuration = getAnimationDuration(pinTypedArray)
        palette = ColorPalette(
            strokeCommonColor,
            strokeErrorColor,
            pinCommonColor,
            pinErrorColor
        )
    }

    private val measurements: Measurements by lazy {
        Measurements(
            pinView.measuredWidth.toFloat(),
            pinSize.partsCount,
            drawingStyle.getCornerRadiusPx(pinView.context),
            pinView.strokeWidth.toFloat()
        )
    }

    fun getPinAnimation(): AnimationLauncher {
        val drawer = AnimationComponentsFactory.createDrawer(
            drawingStyle,
            measurements,
            pinCommonColor
        )
        val animators = AnimationComponentsFactory.createAnimators(
            animationStyle,
            drawer,
            pinView,
            animationDuration,
            palette
        )
        return AnimationLauncher(drawer, animators)
    }

    private fun getPinSize(pinTypedArray: TypedArray): Size {
        return Size.values()
            .find { it.attrValue == pinTypedArray.getInt(R.styleable.PinView_pin_size, -1) }
            ?: Size.MEDIUM
    }

    private fun getDrawingStyle(pinTypedArray: TypedArray): DrawerStyle {
        return DrawerStyle.values()
            .find { it.attrValue == pinTypedArray.getInt(R.styleable.PinView_pin_drawingStyle, -1) }
            ?: DrawerStyle.CIRCLE
    }

    private fun getAnimationStyle(pinTypedArray: TypedArray): AnimationStyle {
        return AnimationStyle.values()
            .find {
                it.attrValue == pinTypedArray.getInt(
                    R.styleable.PinView_pin_animationStyle,
                    -1
                )
            }
            ?: AnimationStyle.COMMON
    }

    private fun getStrokeCommonColor(materialCardTypedArray: TypedArray): ColorStateList {
        val color = materialCardTypedArray.getResourceId(
            com.google.android.material.R.styleable.MaterialCardView_strokeColor, 0
        )
        val defaultColorStateList =
            pinView.resources.getColorStateList(R.color.pin_stroke_color, pinView.context.theme)
        return AppCompatResources.getColorStateList(pinView.context, color)
            ?: defaultColorStateList
    }

    private fun getStrokeErrorColor(): Int {
        val typed = TypedValue()
        pinView.context.theme.resolveAttribute(
            com.google.android.material.R.attr.colorError,
            typed,
            true
        )
        return typed.data
    }

    private fun getPinCommonColor(pinTypedArray: TypedArray): Int {
        return pinTypedArray.getColor(R.styleable.PinView_pin_color, Color.BLACK)
    }

    private fun getAnimationDuration(pinTypedArray: TypedArray): Long {
        return pinTypedArray.getInt(R.styleable.PinView_pin_animationDuration, 1000).toLong()
    }

    private enum class Size(val attrValue: Int, val partsCount: Int) {
        LARGE(1, 12),
        MEDIUM(2, 6),
        SMALL(3, 4)
    }

    internal enum class AnimationStyle(val attrValue: Int) {
        COMMON(1),
        BOUNCING(2),
        COLORING(3)
    }

    internal enum class DrawerStyle(val attrValue: Int, private val cornerRadiusDP: Float) {
        SQUARE(1, 0f),
        ROUNDED_SQUARE(2, 5f),
        CIRCLE(3, 0f);

        fun getCornerRadiusPx(context: Context): Float {
            return context.resources.displayMetrics.density * cornerRadiusDP
        }
    }
}