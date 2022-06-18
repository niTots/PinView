package com.nitots.pinview.animation.animators

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import com.nitots.pinview.animation.drawers.Drawer
import com.nitots.pinview.animation.models.ColorPalette
import com.nitots.pinview.views.PinView

open class ColoringAnimator(
    override val drawer: Drawer,
    override val pinView: PinView,
    override val availableDuration: Long,
    override val palette: ColorPalette
) : Animator {

    override fun updateParams(updated: Float) {
        val color = if (updated == 255f) palette.pinCommonColor else palette.pinErrorColor
        drawer.color(color, updated.toInt())
    }

    private fun updateColor(updatedColor: Int) {
        drawer.color(updatedColor)
    }

    override val subAnimatorsCount: Int
        get() = 2

    override fun createAnimationSet(): AnimatorSet {
        return super.createAnimationSet().apply {
            playSequentially(
                getColoringAnimator(
                    palette.pinCommonColor,
                    palette.pinErrorColor,
                    duration = durationPerAnimator
                ),
                getColoringAnimator(
                    palette.pinErrorColor,
                    palette.pinCommonColor,
                    duration = durationPerAnimator
                )
            )
        }
    }

    private fun getColoringAnimator(
        fromColor: Int,
        toColor: Int,
        duration: Long
    ): ValueAnimator {
        return ValueAnimator.ofObject(
            ArgbEvaluator(),
            fromColor,
            toColor
        ).apply {
            this.duration = duration
            addUpdateListener {
                updateColor((it.animatedValue as? Int) ?: 0)
                pinView.invalidate()
            }
        }
    }
}