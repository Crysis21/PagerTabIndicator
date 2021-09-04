package com.hold1.pagertabsindicator.adapters

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hold1.pagertabsindicator.PagerTabsIndicator
import com.hold1.pagertabsindicator.TabViewProvider

abstract class TabsAdapter() {

    private var context: Context? = null

    var pagerTabsIndicator: PagerTabsIndicator? = null
        set(value) {
            field = value
            context = value?.context
            onAttachedToTabsIndicator()
        }

    var tabViewProvider: TabViewProvider? = null
    set(value) {
        field = value
        pagerTabsIndicator?.invalidateViews()
    }

    abstract fun getCount(): Int

    open fun getTabAt(position: Int): View {
        tabViewProvider?.let { viewProvider ->
            when (viewProvider) {
                is TabViewProvider.ImageResource -> {
                    val imageView = createImageView()
                    viewProvider.getTabIconUri(position)?.let { uri ->
                        context?.let {
                            Glide.with(it)
                                .load(uri)
                                .into(imageView)
                        }
                    } ?: run {
                        imageView.setImageResource(viewProvider.getTabIconResId(position))
                    }
                    return@getTabAt imageView
                }
                is TabViewProvider.ViewResource -> {
                    return@getTabAt viewProvider.getTabView(position)
                }
                is TabViewProvider.TextResource -> {
                    return@getTabAt createTextView(viewProvider.getTabTitle(position))
                }
            }
        }
        throw NotImplementedError("TabViewProvider not set to adapter")
    }

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
