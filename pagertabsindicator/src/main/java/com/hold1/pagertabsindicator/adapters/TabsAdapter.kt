package com.hold1.pagertabsindicator.adapters

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.hold1.pagertabsindicator.PagerTabsIndicator

abstract class TabsAdapter(val context: Context) {

    var pagerTabsIndicator: PagerTabsIndicator? = null
        set(value) {
            field = value
            onAttachedToTabsIndicator()
        }

    abstract fun getCount(): Int
    abstract fun getTabAt(position: Int): View

    private fun onAttachedToTabsIndicator() {
        pagerTabsIndicator?.invalidateViews()
    }

    protected fun createImageView(): ImageView {
        val imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.adjustViewBounds = true
        return imageView
    }

    protected fun createTextView(text: String): View {
        val textView = TextView(context)
        textView.text = text
        textView.gravity = Gravity.CENTER
        textView.setSingleLine()
        pagerTabsIndicator?.let {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (it.textSize).toFloat())
            textView.setTextColor(it.textColor)

        }
//        return object : TabView(this@PagerTabsIndicator.context, textView) {
//            override fun onOffset(offset: Float) {
//                super.onOffset(offset)
//                if (isHighlightText) {
//                    (getChildAt(0) as TextView).setTextColor(
//                            Util.mixTwoColors(
//                                    highlightTextColor,
//                                    textColor,
//                                    offset
//                            )
//                    )
//                }
//            }
//        }
        return textView
    }
}
