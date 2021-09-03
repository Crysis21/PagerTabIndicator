package com.hold1.pagertabsindicator

import android.widget.FrameLayout
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View

/**
 * Created by Cristian Holdunu on 09/11/2017.
 */
open class TabView : FrameLayout {
    private val bgAnimator: ValueAnimator? = null
    private val pressColor = Color.TRANSPARENT
    private val currentBgColor = pressColor
    private var offset = 0f

    constructor(context: Context?) : super(context) {
        setWillNotDraw(false)
    }

    constructor(context: Context?, child: View?) : this(context) {
        addView(child)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setWillNotDraw(false)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setWillNotDraw(false)
    }

    open fun onOffset(offset: Float) {
        this.offset = offset
        invalidate()
    }

    companion object {
        private val TAG = TabView::class.java.simpleName
    }
}