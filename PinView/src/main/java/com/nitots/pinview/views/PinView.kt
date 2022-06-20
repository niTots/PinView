package com.nitots.pinview.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.os.Parcel
import android.os.Parcelable
import android.text.InputType
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.inputmethod.BaseInputConnection
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.core.content.res.use
import com.google.android.material.card.MaterialCardView
import com.nitots.pinview.R
import com.nitots.pinview.animation.AnimationLauncher
import com.nitots.pinview.helpers.PinViewHelper

private val DEF_STYLE_ATTRS = R.attr.pinViewStyle

class PinView(context: Context, attrs: AttributeSet? = null) :
    MaterialCardView(context, attrs, DEF_STYLE_ATTRS) {

    private var pinEnterListener: PinEnterListener? = null

    private lateinit var viewHelper: PinViewHelper


    private val pinBouncingAnimation: AnimationLauncher by lazy {
        viewHelper.getPinAnimation()
    }

    init {
        configure()
        setListeners()
        initUI(context, attrs)
    }

    var value = ""

    private var keyboardKeySender: KeyboardKeySender? = null

    /*
    * In order to configure the IME, this method is overridden.
    * Because of the InputType.TYPE_CLASS_NUMBER type, numbers are not handled within InputConnection class.
    * Instead, they are sent as hard-keyboard event. On the other hand, keycode KeyEvent.KEYCODE_DEL is not sent as hard-keyboard event,
    * but within the InputConnection implementation.
    *
    * To handle both numbers and delete action, I'm setting the OnKeyListener, inside which the numbers are handled and extend the BaseInputConnection
    * class, inside which the overridden method deleteSurroundingText is propagating the call to the PinView's onDeletionClicked method.
    * */
    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection {
        val bic = InnerInputConnection(this)
        outAttrs?.inputType = InputType.TYPE_CLASS_NUMBER
        outAttrs?.imeOptions = EditorInfo.IME_NULL
        return bic
    }

    override fun onCheckIsTextEditor(): Boolean {
        return true
    }

    override fun onSaveInstanceState(): Parcelable? {
        val state = SavedState(super.onSaveInstanceState())
        state.pinValue = value
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        if (state is SavedState) {
            value = state.pinValue
        }

    }


    fun setPinListener(listener: PinEnterListener) {
        this.pinEnterListener = listener
    }

    fun startIncorrectValueAnimation() {
        pinBouncingAnimation.animate()
    }

    fun releaseValue(): Boolean {
        return if (value == "") false
        else {
            this.value = ""
            invalidate()
            true
        }
    }

    private fun configure() {
        isFocusable = true
        isFocusableInTouchMode = true
    }

    private fun setListeners() {
        setOnKeyListener { _, _, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (event?.keyCode) {
                    KeyEvent.KEYCODE_0 -> onValueEntered("0")
                    KeyEvent.KEYCODE_1 -> onValueEntered("1")
                    KeyEvent.KEYCODE_2 -> onValueEntered("2")
                    KeyEvent.KEYCODE_3 -> onValueEntered("3")
                    KeyEvent.KEYCODE_4 -> onValueEntered("4")
                    KeyEvent.KEYCODE_5 -> onValueEntered("5")
                    KeyEvent.KEYCODE_6 -> onValueEntered("6")
                    KeyEvent.KEYCODE_7 -> onValueEntered("7")
                    KeyEvent.KEYCODE_8 -> onValueEntered("8")
                    KeyEvent.KEYCODE_9 -> onValueEntered("9")
                    KeyEvent.KEYCODE_DEL -> onDeletionClicked(KeyboardKeySender.KEY_LISTENER)
                }
            }
            true
        }

    }

    private fun initUI(context: Context, attrs: AttributeSet?) {
        usingTypeArray(context, attrs, R.styleable.PinView) { pinTypedArray ->
            usingTypeArray(
                context,
                attrs,
                com.google.android.material.R.styleable.MaterialCardView
            ) { materialCardTypedArray ->
                viewHelper = PinViewHelper(this, pinTypedArray, materialCardTypedArray)
            }
        }
    }

    private fun usingTypeArray(
        context: Context,
        attrs: AttributeSet?,
        styleable: IntArray,
        block: (TypedArray) -> Unit
    ) {
        context.obtainStyledAttributes(attrs, styleable, DEF_STYLE_ATTRS, 0)
            .use {
                block(it)
            }
    }

    private fun onDeletionClicked(sender: KeyboardKeySender) {
        if (keyboardKeySender == null) {
            keyboardKeySender = sender
            pinEnterListener?.onDeletion()
        } else if (keyboardKeySender == sender) {
            pinEnterListener?.onDeletion()
        }
    }

    private fun onValueEntered(value: String) {
        this.value = value
        invalidate()
        pinEnterListener?.onValueEntered()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
    }


    override fun onDraw(canvas: Canvas?) {
        if (canvas != null && value.isNotEmpty()) drawPin(canvas)
    }

    private fun drawPin(canvas: Canvas) {
        pinBouncingAnimation.draw(canvas)
    }

    interface PinEnterListener {
        fun onDeletion()

        fun onValueEntered()
    }


    private class InnerInputConnection(private val targetView: PinView) :
        BaseInputConnection(targetView, false) {

        override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
            targetView.onDeletionClicked(KeyboardKeySender.INPUT_CONNECTION)
            return super.deleteSurroundingText(beforeLength, afterLength)
        }

    }

    private class SavedState : BaseSavedState {
        var pinValue: String = ""

        constructor(superState: Parcelable?) : super(superState)

        constructor(source: Parcel) : super(source) {
            pinValue = source.readString().orEmpty()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(pinValue)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState> {
                    return arrayOf()
                }
            }
        }
    }

    private enum class KeyboardKeySender {
        KEY_LISTENER,
        INPUT_CONNECTION
    }

    internal companion object {
        @JvmStatic
        internal fun generateLayoutParams(): MarginLayoutParams {
            return MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}