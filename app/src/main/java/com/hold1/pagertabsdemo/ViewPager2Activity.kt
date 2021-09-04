package com.hold1.pagertabsdemo

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hold1.pagertabsdemo.databinding.ActivityViewpager2Binding
import com.hold1.pagertabsdemo.fragments.*
import com.hold1.pagertabsindicator.PagerTabsIndicator
import com.hold1.pagertabsindicator.TabViewProvider
import com.hold1.pagertabsindicator.adapters.ViewPager2TabsAdapter

class ViewPager2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityViewpager2Binding
    private val demoFragments = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewpager2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        demoFragments.add(TabsFragment())
        demoFragments.add(DividerFragment())
        demoFragments.add(ColorsFragment())
        demoFragments.add(AdaptersFragment())
        demoFragments.add(ContactFragment())


        binding.viewPager.adapter = TextAdapter(this)
        binding.tabsIndicator.setAdapter(ViewPager2TabsAdapter(binding.viewPager))
        binding.tabsIndicator.onItemSelectedListener = object: PagerTabsIndicator.OnItemSelectedListener {
            override fun onItemSelected(position: Int) {
                binding.viewPager.setCurrentItem(position, true)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.text -> changeTabAdapter(ViewPagerActivity.TabAdapterType.TEXT)
            R.id.image -> changeTabAdapter(ViewPagerActivity.TabAdapterType.IMAGE)
            R.id.web -> changeTabAdapter(ViewPagerActivity.TabAdapterType.WEB)
            R.id.custom -> changeTabAdapter(ViewPagerActivity.TabAdapterType.CUSTOM)
            R.id.custom_anim -> changeTabAdapter(ViewPagerActivity.TabAdapterType.CUSTOM_ANIM)
        }
        return true
    }

    fun changeTabAdapter(adapterType: ViewPagerActivity.TabAdapterType?) {
        when (adapterType) {
            ViewPagerActivity.TabAdapterType.TEXT -> binding.viewPager.adapter = TextAdapter(this)
            ViewPagerActivity.TabAdapterType.IMAGE -> binding.viewPager.adapter = ImageAdapter(this)
            ViewPagerActivity.TabAdapterType.CUSTOM -> binding.viewPager.adapter = CustomViewAdapter(this)
//            ViewPagerActivity.TabAdapterType.CUSTOM_ANIM -> {
//                binding.viewPager.adapter = viewCustomAnimAdapter
//                binding.tabsIndicator.isShowDivider = false
//                binding.tabsIndicator.showBarIndicator = false
//                binding.tabsIndicator.height =
//                    resources.getDimensionPixelSize(R.dimen.tab_height_min)
//            }
        }
        binding.tabsIndicator.setAdapter(ViewPager2TabsAdapter(binding.viewPager))
    }
    inner class TextAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity), TabViewProvider.TextResource {

        override fun getItemCount(): Int {
            return demoFragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return demoFragments[position]
        }

        override fun getTabTitle(position: Int): String {
            return (demoFragments[position] as? FragmentPresenter)?.tabName ?: "Tab $position"
        }
    }


    inner class ImageAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity), TabViewProvider.ImageResource {

        override fun getItemCount(): Int {
            return demoFragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return demoFragments[position]
        }

        override fun getTabIconUri(position: Int): Uri? {
            return null
        }

        override fun getTabIconResId(position: Int): Int {
            val fragment = demoFragments[position]
            return if (fragment is FragmentPresenter) {
                (fragment as FragmentPresenter).tabImage
            } else R.drawable.ic_add_shopping_cart_black_24dp
        }
    }

    inner class CustomViewAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity), TabViewProvider.ViewResource {

        override fun getItemCount(): Int {
            return demoFragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return demoFragments[position]
        }

        override fun getTabView(position: Int): View {
            val fragment = demoFragments[position]
            if (fragment is FragmentPresenter) {
                val view = layoutInflater.inflate(R.layout.tab_item, null)
                val title = view.findViewById<TextView>(R.id.tab_name)
                title.text = (fragment as FragmentPresenter).tabName
                val icon = view.findViewById<ImageView>(R.id.tab_icon)
                icon.setImageResource((fragment as FragmentPresenter).tabImage)
                return view
            }
            throw NotImplementedError("Improper implementation of CustomView adapter")
        }
    }
}
