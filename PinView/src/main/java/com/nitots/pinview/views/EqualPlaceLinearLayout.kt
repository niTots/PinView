package com.nitots.pinview.views

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

internal class EqualPlaceLinearLayout(context: Context, private val sizeStyleMultiplier: Float) :
    LinearLayout(context) {

    init {
        configure()
    }

    private fun configure() {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
    }

    override fun measureChildWithMargins(
        child: View?,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ) {
        val newChildSize =
            (MeasureSpec.getSize(parentWidthMeasureSpec) / childCount) * sizeStyleMultiplier

        val newChildWidthMeasureSpec =
            MeasureSpec.makeMeasureSpec(
                newChildSize.toInt(),
                MeasureSpec.getMode(parentWidthMeasureSpec)
            )

        val newChildHeightMeasureSpec =
            MeasureSpec.makeMeasureSpec(
                newChildSize.toInt(),
                MeasureSpec.getMode(parentHeightMeasureSpec)
            )

        val lp = child?.layoutParams as? MarginLayoutParams
        val leftMargin = lp?.leftMargin ?: 0
        val rightMargin = lp?.rightMargin ?: 0
        val topMargin = lp?.topMargin ?: 0
        val bottomMargin = lp?.bottomMargin ?: 0
        val childWidth = lp?.width ?: 0
        val childHeight = lp?.height ?: 0

        val widthChildMeasureSpec =
            getChildMeasureSpec(newChildWidthMeasureSpec, leftMargin + rightMargin, childWidth)

        val heightChildMeasureSpec =
            getChildMeasureSpec(
                newChildHeightMeasureSpec,
                topMargin + bottomMargin,
                childHeight
            )

        val resultSpec = if (
            MeasureSpec.getSize(widthChildMeasureSpec) < MeasureSpec.getSize(
                heightChildMeasureSpec
            )
        )
            widthChildMeasureSpec
        else
            heightChildMeasureSpec
        child?.measure(resultSpec, resultSpec)
    }

    internal companion object {
        @JvmStatic
        internal fun generateLayoutParams(): MarginLayoutParams {
            return MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}