package com.nitots.pinview.animation.animators

import android.animation.AnimatorSet
import android.view.animation.BounceInterpolator
import com.nitots.pinview.animation.models.ColorPalette
import com.nitots.pinview.animation.drawers.Drawer
import com.nitots.pinview.views.PinView

open class BouncingAnimator(
    override val drawer: Drawer,
    override val pinView: PinView,
    override val availableDuration: Long,
    override val palette: ColorPalette
) : Animator {

    override val subAnimatorsCount: Int
        get() = 3

    override fun updateParams(updated: Float) {
        drawer.move(updated)
    }

    override fun createAnimationSet(): AnimatorSet {
        return super.createAnimationSet().apply {
            playSequentially(
                getBouncingAnimator(drawer.commonPosition, drawer.startBound),
                getBouncingAnimator(drawer.startBound, drawer.endBound),
                getBouncingAnimator(drawer.endBound, drawer.commonPosition)
            )
        }
    }

    private fun getBouncingAnimator(from: Float, to: Float) =
        getCommonAnimator(from, to, BounceInterpolator(), durationPerAnimator)
}