package com.nitots.pinview.views

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.core.content.res.use
import androidx.core.view.children
import androidx.core.view.isVisible
import com.nitots.pinview.R
import com.nitots.pinview.extensions.focusAndShowKeyboard
import com.nitots.pinview.extensions.hideKeyboard
import com.nitots.pinview.helpers.PinSetViewHelper

private val DEF_STYLE_ATTR = R.attr.pinSetViewStyle

class PinSetView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs, DEF_STYLE_ATTR, 0), PinView.PinEnterListener {

    private var currentIndex = 0

    private lateinit var viewHelper: PinSetViewHelper

    private var completionLister: (String) -> Unit = { _ -> }

    private val innerLinear: EqualPlaceLinearLayout?
        get() = children.find { it is EqualPlaceLinearLayout } as? EqualPlaceLinearLayout

    private val errorLabel: ErrorLabel?
        get() = children.filterIsInstance(ErrorLabel::class.java).firstOrNull()

    private val pins: List<PinView> by lazy {
        innerLinear?.children?.filterIsInstance(PinView::class.java)?.toList().orEmpty()
    }

    init {
        configure()
        initUI(context, attrs)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val state = SavedState(super.onSaveInstanceState())
        state.focusIndex = currentIndex
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        if (state is SavedState) {
            currentIndex = state.focusIndex
        }

    }

    override fun onValueEntered() {
        if (errorLabel?.isVisible == true) toggleVisibility(false)
        val isLastPinEntered = currentIndex == pins.size - 1
        if (isLastPinEntered) {
            pins[pins.size - 1].clearFocus()
            clearFocus()
            hideKeyboard()
            completionLister.invoke(collectValues())
        } else {
            currentIndex++
            pins[currentIndex].focusAndShowKeyboard()
        }
    }

    override fun onDeletion() {
        if (!pins[currentIndex].releaseValue()) {
            if (currentIndex > 0)
                pins[--currentIndex].also { pin ->
                    pin.releaseValue()
                    pin.focusAndShowKeyboard()
                }
        }
    }

    fun setCompletionListener(listener: (String) -> Unit) {
        this.completionLister = listener
    }

    fun incorrectPinEntered() {
        toggleVisibility(true)
        pins.forEach { it.startIncorrectValueAnimation() }
        pins[pins.size - 1].focusAndShowKeyboard()
    }

    private fun configure() {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        isClickable = true
        setOnClickListener {
            pins[currentIndex].focusAndShowKeyboard()
        }
    }

    private fun initUI(context: Context, attrs: AttributeSet?) {
        initPinSetViewHelper(context, attrs)
        with(viewHelper) {
            addPins(this@PinSetView, context)
            addErrorLabel(this@PinSetView, context)
        }
        initPins()
    }

    private fun initPinSetViewHelper(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.PinSetView, DEF_STYLE_ATTR, 0)
            .use { pinSetViewTypedArray ->
                viewHelper = PinSetViewHelper(pinSetViewTypedArray)
            }
    }

    private fun initPins() {
        pins.forEach { pin -> pin.setPinListener(this) }
        pins[currentIndex].focusAndShowKeyboard()
    }

    private fun toggleVisibility(show: Boolean) {
        errorLabel?.visibility = if (show) VISIBLE else INVISIBLE
    }

    private fun collectValues(): String {
        val builder = StringBuilder()
        pins.forEach { pin ->
            builder.append(pin.value)
        }
        return builder.toString()
    }

    private class SavedState : BaseSavedState {
        var focusIndex: Int = 0

        constructor(superState: Parcelable?) : super(superState)

        constructor(source: Parcel) : super(source) {
            focusIndex = source.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(focusIndex)
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
}