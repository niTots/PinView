package com.nitots.pinview.animation.models

import android.content.res.ColorStateList
import androidx.annotation.ColorInt

data class ColorPalette(
    val strokeCommonColor: ColorStateList,
    @ColorInt val strokeErrorColor: Int,
    @ColorInt val pinCommonColor: Int,
    @ColorInt val pinErrorColor: Int,
)