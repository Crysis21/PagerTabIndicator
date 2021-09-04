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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.hold1.pagertabsdemo.databinding.ActivityMainBinding
import com.hold1.pagertabsdemo.fragments.*
import com.hold1.pagertabsindicator.PagerTabsIndicator
import com.hold1.pagertabsindicator.TabViewProvider
import com.hold1.pagertabsindicator.TabViewProvider.ViewResource
import com.hold1.pagertabsindicator.adapters.ViewPagerTabsAdapter
import java.util.*

class ViewPagerActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    //Just for easing the demo :)
    val tabsIndicator: PagerTabsIndicator
        get() {
            return binding.tabsIndicator
        }

    private var viewImageAdapter: PagerAdapter? = null
    private var webImageAdapter: PagerAdapter? = null
    private var viewCustomAdapter: PagerAdapter? = null
    private var viewCustomAnimAdapter: PagerAdapter? = null
    private var viewTextAdapter: PagerAdapter? = null
    private val demoFragments: MutableList<Fragment> = ArrayList()

    enum class TabAdapterType {
        TEXT, IMAGE, WEB, CUSTOM, CUSTOM_ANIM
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        demoFragments.add(TabsFragment())
        demoFragments.add(DividerFragment())
        demoFragments.add(ColorsFragment())
        demoFragments.add(AdaptersFragment())
        demoFragments.add(ContactFragment())
        viewTextAdapter = TextAdapter(supportFragmentManager)
        viewImageAdapter = ImageAdapter(supportFragmentManager)
        webImageAdapter = WebImageAdapter(supportFragmentManager)
        viewCustomAdapter = AdapterResource(supportFragmentManager)
        viewCustomAnimAdapter = AnimAdapterResource(supportFragmentManager)
        binding.viewPager.adapter = viewCustomAdapter
        tabsIndicator.setAdapter(ViewPagerTabsAdapter(binding.viewPager))
        tabsIndicator.onItemSelectedListener = object : PagerTabsIndicator.OnItemSelectedListener {
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
            R.id.text -> changeTabAdapter(TabAdapterType.TEXT)
            R.id.image -> changeTabAdapter(TabAdapterType.IMAGE)
            R.id.web -> changeTabAdapter(TabAdapterType.WEB)
            R.id.custom -> changeTabAdapter(TabAdapterType.CUSTOM)
            R.id.custom_anim -> changeTabAdapter(TabAdapterType.CUSTOM_ANIM)
        }
        return true
    }

    fun changeTabAdapter(adapterType: TabAdapterType?) {
        binding.viewPager.removeAllViews()
        when (adapterType) {
            TabAdapterType.TEXT -> binding.viewPager.adapter = viewTextAdapter
            TabAdapterType.IMAGE -> binding.viewPager.adapter = viewImageAdapter
            TabAdapterType.WEB -> binding.viewPager.adapter = webImageAdapter
            TabAdapterType.CUSTOM -> binding.viewPager.adapter = viewCustomAdapter
            TabAdapterType.CUSTOM_ANIM -> {
                binding.viewPager.adapter = viewCustomAnimAdapter
                binding.tabsIndicator.isShowDivider = false
                binding.tabsIndicator.showBarIndicator = false
                binding.tabsIndicator.height =
                    resources.getDimensionPixelSize(R.dimen.tab_height_min)
            }
        }
    }

    internal inner class ImageAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!),
        TabViewProvider.ImageResource {
        override fun getItem(position: Int): Fragment {
            return demoFragments[position]
        }

        override fun getCount(): Int {
            return demoFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return "Tab $position"
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

    internal inner class WebImageAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!),
        TabViewProvider.ImageResource {
        override fun getItem(position: Int): Fragment {
            return demoFragments[position]
        }

        override fun getCount(): Int {
            return demoFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return "Tab $position"
        }

        override fun getTabIconUri(position: Int): Uri? {
            val fragment = demoFragments[position]
            return if (fragment is FragmentPresenter) {
                Uri.parse((fragment as FragmentPresenter).tabImageUrl)
            } else Uri.parse("https://icons8.github.io/flat-color-icons/svg/opened_folder.svg")
        }

        override fun getTabIconResId(position: Int): Int {
            val fragment = demoFragments[position]
            return if (fragment is FragmentPresenter) {
                (fragment as FragmentPresenter).tabImage
            } else R.drawable.ic_add_shopping_cart_black_24dp
        }
    }

    internal inner class TextAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
        override fun getItem(position: Int): Fragment {
            return demoFragments[position]
        }

        override fun getCount(): Int {
            return demoFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            val fragment = demoFragments[position]
            return if (fragment is FragmentPresenter) {
                (fragment as FragmentPresenter).tabName
            } else "Tab $position"
        }
    }

    internal inner class AdapterResource(fm: FragmentManager?) : FragmentPagerAdapter(fm!!),
        ViewResource {
        override fun getItem(position: Int): Fragment {
            return demoFragments[position]
        }

        override fun getCount(): Int {
            return demoFragments.size
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

    internal inner class AnimAdapterResource(fm: FragmentManager?) : FragmentPagerAdapter(fm!!),
        ViewResource {
        override fun getItem(position: Int): Fragment {
            return demoFragments[position]
        }

        override fun getCount(): Int {
            return demoFragments.size
        }

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

    fun addDummyTab() {
        demoFragments.add(DemoFragment())
        binding.viewPager.adapter?.notifyDataSetChanged()
    }
}