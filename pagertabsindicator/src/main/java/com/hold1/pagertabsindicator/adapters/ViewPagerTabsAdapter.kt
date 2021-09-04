package com.hold1.pagertabsindicator.adapters

import android.database.DataSetObserver
import android.util.Log
import android.view.View
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.hold1.pagertabsindicator.TabViewProvider

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
                    return@getTabAt createTextView(pagerAdapter.getPageTitle(position).toString())
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
