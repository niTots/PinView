package com.nitots.pinview.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import com.nitots.pinview.R

private val DEF_STYLE_ATTR = R.attr.pinSetErrorLabelStyle

internal class ErrorLabel(context: Context, attrs: AttributeSet? = null) :
    TextView(context, attrs, DEF_STYLE_ATTR, 0) {
    init {
        configure()
    }

    private fun configure() {
        visibility = INVISIBLE
    }

    internal companion object {
        @JvmStatic
        internal fun generateLayoutParams(): ViewGroup.MarginLayoutParams {
            return ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}