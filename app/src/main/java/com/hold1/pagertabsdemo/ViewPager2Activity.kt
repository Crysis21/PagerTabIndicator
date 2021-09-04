package com.hold1.pagertabsdemo

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewpager2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val pagerAdapter = DemoAdapter(this)
        pagerAdapter.fragments.add(TabsFragment::class.java)
        pagerAdapter.fragments.add(DividerFragment::class.java)
        pagerAdapter.fragments.add(ColorsFragment::class.java)
        pagerAdapter.fragments.add(AdaptersFragment::class.java)
        pagerAdapter.fragments.add(ContactFragment::class.java)

        binding.viewPager.adapter = pagerAdapter
        binding.tabsIndicator.setAdapter(ViewPager2TabsAdapter(binding.viewPager))
        binding.tabsIndicator.onItemSelectedListener = object: PagerTabsIndicator.OnItemSelectedListener {
            override fun onItemSelected(position: Int) {
                binding.viewPager.setCurrentItem(position, true)
            }
        }
    }
}

class DemoAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity), TabViewProvider.TextResource {

    var fragments = mutableListOf<Class<out Fragment>>()

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position].newInstance()
    }

    override fun getTabTitle(position: Int): String {
        return "Test"
    }
}