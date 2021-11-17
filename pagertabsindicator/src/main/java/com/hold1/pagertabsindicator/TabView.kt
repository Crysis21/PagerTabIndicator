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
open class TabView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private val bgAnimator: ValueAnimator? = null
    private val pressColor = Color.TRANSPARENT
    private val currentBgColor = pressColor
    private var offset = 0f

    init {
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