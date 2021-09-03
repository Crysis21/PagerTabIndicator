package com.hold1.pagertabsindicator

import android.content.Context
import android.database.DataSetObserver
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide

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
//        pagerTabsIndicator?.let {
//            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (it.textSize).toFloat())
//            textView.setTextColor(it.textColor)
//
//        }
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

class ViewPagerTabsAdapter(viewPager: ViewPager) : TabsAdapter(viewPager.context), ViewPager.OnPageChangeListener {
    private var adapter: PagerAdapter? = null

    private var adapterChangeListener: ViewPager.OnAdapterChangeListener = ViewPager.OnAdapterChangeListener { _, _, newAdapter ->
        listenToAdapterChanges(newAdapter)
    }
    private var adapterObserver: DataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            pagerTabsIndicator?.invalidateViews()
        }
    }

    init {
        viewPager.addOnPageChangeListener(this)
        viewPager.addOnAdapterChangeListener(adapterChangeListener)
        listenToAdapterChanges(viewPager.adapter)
    }

    private fun listenToAdapterChanges(pagerAdapter: PagerAdapter?) {
        //remove old adapter if present
        adapter?.unregisterDataSetObserver(adapterObserver)
        adapter = pagerAdapter
        adapter?.registerDataSetObserver(adapterObserver)
        pagerTabsIndicator?.invalidateViews()
    }

    override fun getCount(): Int {
        return adapter?.count ?: 0
    }

    override fun getTabAt(position: Int): View {
        Log.d("TabViewProvider", "getTabAt $position adapter=$adapter")
        adapter?.let { pagerAdapter ->
            when (pagerAdapter) {
                is TabViewProvider.ImageProvider -> {
                    val imageView = createImageView()
                    Glide.with(context)
                            .load(pagerAdapter.getImageUri(position))
                            .into(imageView)
                    return@getTabAt imageView
                }
                is TabViewProvider.CustomView -> {
                    return@getTabAt pagerAdapter.getView(position)
                }
                else -> {
                    return@getTabAt createTextView(adapter!!.getPageTitle(position).toString())
                }
            }
        }
        throw NotImplementedError("Adapter is null")
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        pagerTabsIndicator?.onPageScrolled(position, positionOffset, positionOffsetPixels)
    }

    override fun onPageSelected(position: Int) {
        pagerTabsIndicator?.onPageSelected(position)
    }

    override fun onPageScrollStateChanged(state: Int) {
        pagerTabsIndicator?.onPageScrollStateChanged(state)
    }
}

sealed class TabViewProvider() {

    interface ImageProvider {
        fun getImageUri(position: Int): Uri?
        fun getImageResourceId(position: Int): Int
    }

    interface CustomView {
        fun getView(position: Int): View
    }
}