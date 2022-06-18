package com.nitots.pinview.animation

import android.animation.AnimatorSet
import android.graphics.Canvas
import com.nitots.pinview.animation.animators.Animator
import com.nitots.pinview.animation.drawers.Drawer

internal class AnimationLauncher(
    private val drawer: Drawer,
    private val animators: List<Animator>
) {
    private val animationSet = AnimatorSet().apply {
        playTogether(animators.map { it.createAnimationSet() })
    }

    fun draw(canvas: Canvas) {
        drawer.draw(canvas)
    }

    fun animate() {
        animationSet.start()
    }
}