package com.nitots.pinview.animation.animators

import android.animation.AnimatorSet
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import com.nitots.pinview.animation.models.ColorPalette
import com.nitots.pinview.animation.drawers.Drawer
import com.nitots.pinview.views.PinView

interface Animator {
    val pinView: PinView?
    val drawer: Drawer
    val availableDuration: Long
    val palette: ColorPalette?
    val subAnimatorsCount: Int
    val durationPerAnimator: Long
        get() = availableDuration / subAnimatorsCount

    fun updateParams(updated: Float) {}

    fun createAnimationSet(): AnimatorSet {
        return AnimatorSet()
    }

    fun animate() {
        createAnimationSet().start()
    }

    fun getCommonAnimator(
        from: Float,
        to: Float,
        timeInterpolator: TimeInterpolator? = null,
        duration: Long,
        forceInvalidate: Boolean = true
    ): ValueAnimator {
        return ValueAnimator.ofFloat(from, to).apply {
            this.duration = duration
            timeInterpolator?.let { interpolator = it }
            addUpdateListener { updatedAnimation ->
                updateParams((updatedAnimation.animatedValue as? Float) ?: 0f)
                if (forceInvalidate) pinView?.invalidate()
            }
        }
    }
}