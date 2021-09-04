package com.hold1.pagertabsindicator.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.hold1.pagertabsindicator.TabViewProvider

class ViewPager2TabsAdapter(val viewPager2: ViewPager2) : TabsAdapter(viewPager2.context) {
    private val pagerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> = viewPager2.adapter!!

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            pagerTabsIndicator?.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            pagerTabsIndicator?.onPageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            pagerTabsIndicator?.onPageScrollStateChanged(state)
        }
    }

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            pagerTabsIndicator?.invalidateViews()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            pagerTabsIndicator?.invalidateViews()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            pagerTabsIndicator?.invalidateViews()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            pagerTabsIndicator?.invalidateViews()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            pagerTabsIndicator?.invalidateViews()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            pagerTabsIndicator?.invalidateViews()
        }
    }

    init {
        pagerAdapter.registerAdapterDataObserver(adapterDataObserver)
        viewPager2.registerOnPageChangeCallback(onPageChangeCallback)
    }

    override fun getCount(): Int {
        return pagerAdapter.itemCount
    }

    override fun getTabAt(position: Int): View {
        return when (pagerAdapter) {
            is TabViewProvider.ImageResource -> {
                val imageView = createImageView()
                pagerAdapter.getTabIconUri(position)?.let { uri ->
                    Glide.with(context)
                        .load(uri)
                        .into(imageView)
                } ?: run {
                    imageView.setImageResource(
                        (pagerAdapter as TabViewProvider.ImageResource).getTabIconResId(
                            position
                        )
                    )
                }
                imageView
            }
            is TabViewProvider.ViewResource -> {
                pagerAdapter.getTabView(position)
            }
            is TabViewProvider.TextResource -> {
                createTextView(pagerAdapter.getTabTitle(position))
            }
            else -> {
                throw NotImplementedError("Can't generate a view tab")
            }
        }
    }

}