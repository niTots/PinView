package com.nitots.pinview.helpers

import android.content.Context
import android.content.res.TypedArray
import android.view.ViewGroup
import androidx.core.view.updateMargins
import com.nitots.pinview.R
import com.nitots.pinview.views.EqualPlaceLinearLayout
import com.nitots.pinview.views.ErrorLabel
import com.nitots.pinview.views.PinSetView
import com.nitots.pinview.views.PinView
import kotlin.math.max

internal class PinSetViewHelper(pinSetViewTypedArray: TypedArray) {
    private val errorLabelExists: Boolean
    private val errorLabelPosition: ErrorLabelPosition
    private val pinCount: Int
    private val sizeStyle: SizeStyle
    private val pinHorizontalMargin: Int
    private val errorLabelMargin: Int


    init {
        errorLabelExists =
            pinSetViewTypedArray.getBoolean(R.styleable.PinSetView_pinSet_isLabelShown, true)
        errorLabelPosition = getErrorLabelPosition(pinSetViewTypedArray)
        pinCount = max(1, pinSetViewTypedArray.getInt(R.styleable.PinSetView_pinSet_pinCount, 4))
        sizeStyle = getSizeStyle(pinSetViewTypedArray)
        pinHorizontalMargin =
            pinSetViewTypedArray.getDimensionPixelSize(
                R.styleable.PinSetView_pinSet_horizontalMargin,
                0
            )
        errorLabelMargin =
            pinSetViewTypedArray.getDimensionPixelSize(
                R.styleable.PinSetView_pinSet_labelMargin,
                0
            )
    }

    fun addPins(parent: PinSetView, context: Context) {
        val wrapper = EqualPlaceLinearLayout(context, sizeStyle.multiplier)
        val lp = generateEqualPlaceLinearLayoutLayoutParams()
        for (i in 1..pinCount) {
            val pin = PinView(context)
            val lp = generatePinViewLayoutParams()
            wrapper.addView(pin, lp)
        }
        parent.addView(wrapper, lp)
    }

    fun addErrorLabel(parent: PinSetView, context: Context) {
        if (errorLabelExists) {
            val errorLabel = ErrorLabel(context)
            val lp = generateErrorLabelLayoutParams()
            parent.addView(errorLabel, errorLabelPosition.value, lp)
        }
    }

    private fun generateErrorLabelLayoutParams(): ViewGroup.MarginLayoutParams {
        return ErrorLabel.generateLayoutParams().also {
            if (errorLabelPosition == ErrorLabelPosition.ABOVE)
                it.updateMargins(bottom = errorLabelMargin)
            else
                it.updateMargins(top = errorLabelMargin)
        }
    }

    private fun generatePinViewLayoutParams(): ViewGroup.MarginLayoutParams {
        return PinView.generateLayoutParams().also {
            it.updateMargins(
                left = pinHorizontalMargin,
                right = pinHorizontalMargin
            )
        }
    }

    private fun generateEqualPlaceLinearLayoutLayoutParams(): ViewGroup.MarginLayoutParams {
        return EqualPlaceLinearLayout.generateLayoutParams()
    }

    private fun getSizeStyle(pinSetViewTypedArray: TypedArray): SizeStyle {
        return SizeStyle.values()
            .find {
                it.attrValue == pinSetViewTypedArray.getInt(
                    R.styleable.PinSetView_pinSet_size,
                    1
                )
            } ?: SizeStyle.LARGE
    }

    private fun getErrorLabelPosition(errorTypedArray: TypedArray): ErrorLabelPosition {
        return ErrorLabelPosition.values()
            .find {
                it.attrValue == errorTypedArray.getInt(
                    R.styleable.PinSetView_pinSet_labelPosition,
                    -1
                )
            } ?: ErrorLabelPosition.BELOW
    }

    private enum class SizeStyle(val attrValue: Int, val multiplier: Float) {
        LARGE(1, 1f),
        MEDIUM(2, 0.8f),
        SMALL(3, 0.6f)
    }

    private enum class ErrorLabelPosition(val attrValue: Int, val value: Int) {
        ABOVE(1, 0),
        BELOW(2, -1)
    }
}