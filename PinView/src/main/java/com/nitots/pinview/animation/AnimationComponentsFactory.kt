package com.nitots.pinview.animation

import com.nitots.pinview.animation.animators.Animator
import com.nitots.pinview.animation.animators.BouncingAnimator
import com.nitots.pinview.animation.animators.ColoringAnimator
import com.nitots.pinview.animation.animators.StrokeAnimator
import com.nitots.pinview.animation.drawers.CircleDrawer
import com.nitots.pinview.animation.drawers.Drawer
import com.nitots.pinview.animation.drawers.SquareDrawer
import com.nitots.pinview.animation.models.ColorPalette
import com.nitots.pinview.animation.models.Measurements
import com.nitots.pinview.helpers.PinViewHelper
import com.nitots.pinview.views.PinView

internal object AnimationComponentsFactory {
    fun createDrawer(
        drawerStyle: PinViewHelper.DrawerStyle,
        measurements: Measurements,
        pinColor: Int
    ): Drawer {
        return when (drawerStyle) {
            PinViewHelper.DrawerStyle.SQUARE -> SquareDrawer(
                measurements,
                pinColor
            )

            PinViewHelper.DrawerStyle.ROUNDED_SQUARE -> SquareDrawer(
                measurements,
                pinColor
            )

            PinViewHelper.DrawerStyle.CIRCLE -> CircleDrawer(
                measurements,
                pinColor
            )
        }
    }

    fun createAnimators(
        animatorStyle: PinViewHelper.AnimationStyle,
        drawer: Drawer,
        pinView: PinView,
        availableDuration: Long,
        palette: ColorPalette
    ): List<Animator> {
        return when (animatorStyle) {
            PinViewHelper.AnimationStyle.COMMON -> listOf(
                StrokeAnimator(
                    drawer,
                    pinView,
                    availableDuration,
                    palette
                )
            )
            PinViewHelper.AnimationStyle.BOUNCING -> listOf(
                StrokeAnimator(
                    drawer,
                    pinView,
                    availableDuration,
                    palette
                ),
                BouncingAnimator(
                    drawer,
                    pinView,
                    availableDuration,
                    palette
                )
            )
            PinViewHelper.AnimationStyle.COLORING -> listOf(
                StrokeAnimator(
                    drawer,
                    pinView,
                    availableDuration,
                    palette
                ),
                ColoringAnimator(
                    drawer,
                    pinView,
                    availableDuration,
                    palette
                )
            )
        }
    }
}