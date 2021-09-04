package com.hold1.pagertabsdemo.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import com.hold1.pagertabsdemo.ViewPagerActivity
import com.hold1.pagertabsdemo.R
import com.hold1.pagertabsdemo.databinding.TabsFragmentBinding
import com.hold1.pagertabsindicator.PagerTabsIndicator

/**
 * Created by Cristian Holdunu on 09/11/2017.
 */
class TabsFragment : Fragment(), FragmentPresenter {
    var tabsIndicator: PagerTabsIndicator? = null

    private lateinit var binding: TabsFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = TabsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabsIndicator = (activity as? ViewPagerActivity)?.tabsIndicator
        binding.textSize.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                tabsIndicator?.textSize = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        binding.tabPadding.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                tabsIndicator?.tabPadding = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        binding.tabElevation.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                tabsIndicator?.tabElevation = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        binding.indicatorHeight.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                tabsIndicator?.indicatorHeight = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        binding.indicatorBgHeight.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                tabsIndicator?.indicatorBgHeight = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        binding.indicatorMargin.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                tabsIndicator?.indicatorMargin = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        binding.lockExpanded.setOnCheckedChangeListener { _, isChecked -> tabsIndicator?.isLockExpanded = isChecked }
        binding.indicatorType.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.bar_top) {
                tabsIndicator?.indicatorType = PagerTabsIndicator.TAB_INDICATOR_TOP
            } else {
                tabsIndicator?.indicatorType = PagerTabsIndicator.TAB_INDICATOR_BOTTOM
            }
        }
        binding.addDummy.setOnClickListener { (activity as? ViewPagerActivity)?.addDummyTab() }
        binding.showBarIndicator.setOnCheckedChangeListener { _, isChecked -> tabsIndicator?.showBarIndicator = isChecked }
        binding.highlightText.setOnCheckedChangeListener { _, isChecked ->
            tabsIndicator?.isHighlightText = isChecked
            tabsIndicator?.highlightTextColor = Color.RED
        }
    }

    override val tabName: String
        get() = "Tabs"
    override val tabImage: Int
        get() = R.drawable.ic_tabs
    override val tabImageUrl: String
        get() = "https://s3-us-west-2.amazonaws.com/anaface-pictures/ic_tab.png"
}