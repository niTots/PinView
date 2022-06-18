package com.nitots.pinview.animation.animators

import android.animation.AnimatorSet
import com.nitots.pinview.animation.drawers.Drawer
import com.nitots.pinview.animation.models.ColorPalette
import com.nitots.pinview.views.PinView

open class StrokeAnimator(
    override val drawer: Drawer,
    override val pinView: PinView,
    override val availableDuration: Long,
    override val palette: ColorPalette
) : Animator {

    override val subAnimatorsCount: Int
        get() = 1

    override fun updateParams(updated: Float) {
        if (updated < 1f) pinView.strokeColor = palette.strokeErrorColor
        else pinView.setStrokeColor(palette.strokeCommonColor)
    }

    override fun createAnimationSet(): AnimatorSet {
        return super.createAnimationSet().apply {
            play(
                getCommonAnimator(0f, 1f, duration = durationPerAnimator, forceInvalidate = false)
            )
        }
    }
}