package com.hold1.pagertabsindicator.adapters

import android.database.DataSetObserver
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class ViewPagerTabsAdapter(viewPager: ViewPager) : TabsAdapter(), ViewPager.OnPageChangeListener {
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
