package com.hold1.pagertabsdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hold1.pagertabsdemo.ViewPagerActivity
import com.hold1.pagertabsdemo.R
import com.hold1.pagertabsdemo.databinding.ColorsFragmentBinding
import com.hold1.pagertabsindicator.PagerTabsIndicator

/**
 * Created by Cristian Holdunu on 08/11/2017.
 */
class ColorsFragment : Fragment(), FragmentPresenter {
    private var tabsIndicator: PagerTabsIndicator? = null

    private lateinit var binding: ColorsFragmentBinding

    private var colorClick: View.OnClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ColorsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabsIndicator = (activity as? ViewPagerActivity)?.tabsIndicator
        colorClick = View.OnClickListener { v ->
            when (v.id) {
                R.id.color_text -> tabsIndicator?.textColor = binding.colorPickerView.selectedColor
                R.id.color_ind_bg -> tabsIndicator?.indicatorBgColor = binding.colorPickerView.selectedColor
                R.id.color_ind -> tabsIndicator?.indicatorColor = binding.colorPickerView.selectedColor
                R.id.color_div -> tabsIndicator?.dividerColor = binding.colorPickerView.selectedColor
                R.id.color_bg -> tabsIndicator?.setBackgroundColor(binding.colorPickerView.selectedColor)
                R.id.highlight_color -> tabsIndicator?.highlightTextColor = binding.colorPickerView.selectedColor
            }
            tabsIndicator!!.refresh()
        }
        for (i in 0 until binding.elements.childCount) binding.elements.getChildAt(i).setOnClickListener(colorClick)
    }

    override val tabName: String = "Colors"
    override val tabImage: Int = R.drawable.ic_colors
    override val tabImageUrl: String? = null
}