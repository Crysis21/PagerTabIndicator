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
import com.hold1.pagertabsindicator.adapters.ViewPagerTabsAdapter

class ViewPager2Activity : AppCompatActivity(), TabbedActivity {

    private lateinit var binding: ActivityViewpager2Binding
    private val demoFragments = mutableListOf<Fragment>()
    private var viewPagerTabsAdapter: ViewPager2TabsAdapter? = null

    //Just for easing the demo :)
    override val tabsIndicator: PagerTabsIndicator
        get() {
            return binding.tabsIndicator
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewpager2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        demoFragments.add(TabsFragment())
        demoFragments.add(DividerFragment())
        demoFragments.add(ColorsFragment())
        demoFragments.add(AdaptersFragment())
        demoFragments.add(ContactFragment())

        binding.viewPager.adapter = DemoPagerAdapter(this)

        viewPagerTabsAdapter = ViewPager2TabsAdapter(binding.viewPager)
        viewPagerTabsAdapter?.tabViewProvider = viewProvider

        binding.tabsIndicator.setAdapter(viewPagerTabsAdapter)
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
            R.id.text -> changeTabAdapter(TabbedActivity.TabAdapterType.TEXT)
            R.id.image -> changeTabAdapter(TabbedActivity.TabAdapterType.IMAGE)
            R.id.web -> changeTabAdapter(TabbedActivity.TabAdapterType.WEB)
            R.id.custom -> changeTabAdapter(TabbedActivity.TabAdapterType.CUSTOM)
            R.id.custom_anim -> changeTabAdapter(TabbedActivity.TabAdapterType.CUSTOM_ANIM)
        }
        return true
    }

    override fun changeTabAdapter(adapterType: TabbedActivity.TabAdapterType?) {
        when (adapterType) {
            TabbedActivity.TabAdapterType.TEXT -> viewPagerTabsAdapter?.tabViewProvider = textProvider
            TabbedActivity.TabAdapterType.IMAGE -> viewPagerTabsAdapter?.tabViewProvider = imageProvider
            TabbedActivity.TabAdapterType.WEB -> viewPagerTabsAdapter?.tabViewProvider = webImageProvider
            TabbedActivity.TabAdapterType.CUSTOM -> viewPagerTabsAdapter?.tabViewProvider = viewProvider
            TabbedActivity.TabAdapterType.CUSTOM_ANIM -> viewPagerTabsAdapter?.tabViewProvider = customAnimationProvider
        }
    }

    inner class DemoPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity), TabViewProvider.TextResource {

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


    private val imageProvider = object : TabViewProvider.ImageResource {
        override fun getTabIconUri(position: Int): Uri? {
            return null
        }

        override fun getTabIconResId(position: Int): Int {
            return (demoFragments[position] as? FragmentPresenter)?.tabImage
                ?: R.drawable.ic_add_shopping_cart_black_24dp
        }
    }

    private val webImageProvider = object : TabViewProvider.ImageResource {
        override fun getTabIconUri(position: Int): Uri? {
            val fragment = demoFragments[position]
            return Uri.parse(
                (fragment as? FragmentPresenter)?.tabImageUrl
                    ?: "https://icons8.github.io/flat-color-icons/svg/opened_folder.svg"
            )
        }

        override fun getTabIconResId(position: Int): Int {
            val fragment = demoFragments[position]
            return if (fragment is FragmentPresenter) {
                (fragment as FragmentPresenter).tabImage
            } else R.drawable.ic_add_shopping_cart_black_24dp
        }
    }


    private val textProvider = object : TabViewProvider.TextResource {

        override fun getTabTitle(position: Int): String {
            return (demoFragments[position] as? FragmentPresenter)?.tabName ?: "Tab $position"
        }
    }


    private val viewProvider = object : TabViewProvider.ViewResource {
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

    private val customAnimationProvider = object : TabViewProvider.ViewResource {

        override fun getTabView(position: Int): View {
            val fragment = demoFragments[position]
            return if (fragment is FragmentPresenter) {
                TabItemView(
                    applicationContext,
                    (fragment as FragmentPresenter).tabName,
                    (fragment as FragmentPresenter).tabImage,
                    -0xc9c9ca,
                    -0x10000
                )
            } else throw NotImplementedError("Improper implementation of CustomView adapter")
        }
    }

    override fun addDummyTab() {
        demoFragments.add(DemoFragment())
        binding.viewPager.adapter?.notifyDataSetChanged()
    }
}
