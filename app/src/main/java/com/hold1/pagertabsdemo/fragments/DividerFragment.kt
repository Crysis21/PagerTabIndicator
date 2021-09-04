package com.hold1.pagertabsdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import com.hold1.pagertabsdemo.TabbedActivity
import com.hold1.pagertabsdemo.R
import com.hold1.pagertabsdemo.databinding.DividerFragmentBinding
import com.hold1.pagertabsindicator.PagerTabsIndicator

/**
 * Created by Cristian Holdunu on 08/11/2017.
 */
class DividerFragment : Fragment(), FragmentPresenter {
    private var tabsIndicator: PagerTabsIndicator? = null

    private lateinit var binding: DividerFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DividerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabsIndicator = (activity as? TabbedActivity)?.tabsIndicator
        binding.divWidth.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                tabsIndicator!!.dividerWidth = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        binding.divMargin.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                tabsIndicator?.dividerMargin = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        binding.showDivider.setOnCheckedChangeListener { _, isChecked -> tabsIndicator!!.isShowDivider = isChecked }
        binding.solidColor.setOnClickListener {
            tabsIndicator?.dividerResource = -1
            tabsIndicator?.dividerWidth = 1
        }
        binding.divider1.setOnClickListener {
            tabsIndicator?.dividerResource = R.drawable.ic_divider1
            tabsIndicator?.dividerWidth = 20
            tabsIndicator?.dividerMargin = 40
        }
        binding.divider2.setOnClickListener {
            tabsIndicator?.dividerResource = R.drawable.ic_divider2
            tabsIndicator?.dividerWidth = 18
            tabsIndicator?.dividerMargin = 30
        }
        binding.divider3.setOnClickListener {
            tabsIndicator?.dividerResource = R.drawable.ic_divider3
            tabsIndicator?.dividerWidth = 40
            tabsIndicator?.dividerMargin = 36
        }
    }

    override val tabName: String
        get() = "Divider"
    override val tabImage: Int
        get() = R.drawable.ic_divider
    override val tabImageUrl: String
        get() = "https://s3-us-west-2.amazonaws.com/anaface-pictures/ic_divider.png"
}