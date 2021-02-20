package io.github.slflfl12.customviewanimation.drawable

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import androidx.appcompat.widget.AppCompatImageView
import io.github.slflfl12.customviewanimation.R

class CheckedImageView : AppCompatImageView, Checkable {

    private var mChecked: Boolean = false
    private var mBroadCasting: Boolean = false

    private var onCheckedChangeListener: OnCheckedChangeListener? = null

    private var CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CheckedImageView, defStyleAttr, 0)

        val checked = typedArray.getBoolean(R.styleable.CheckedImageView_checked, false)
        setChecked(checked)

        typedArray.recycle()

    }


    override fun isChecked(): Boolean = mChecked


    override fun toggle() {
        isChecked = !mChecked
    }

    override fun setChecked(checked: Boolean) {
        if(mChecked != checked) {
            mChecked = checked
            refreshDrawableState()

            // Avoid infinite recursions if setChecked() is called from a listener

            // Avoid infinite recursions if setChecked() is called from a listener
            if (mBroadCasting) {
                return
            }
            mBroadCasting = true

            onCheckedChangeListener?.onCheckedChanged(this, true)
            mBroadCasting = false
        }
    }

    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener) {
        this.onCheckedChangeListener = listener
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(checkedImageView: CheckedImageView, isChecked: Boolean)
    }

    //refreshDrawableState()를 호출하면 onCreateDrawableState()가 호출됨
    override fun onCreateDrawableState(extraSpace: Int): IntArray { // 코드 상에서 view의 state를 관리하는 함수 (ex. pressed, selected...)
        val drawableState = super.onCreateDrawableState(extraSpace + CHECKED_STATE_SET.size)
        if(mChecked) {
            View.mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }
        return drawableState
    }
}