package com.hold1.pagertabsdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hold1.pagertabsdemo.TabbedActivity
import com.hold1.pagertabsdemo.R
import com.hold1.pagertabsdemo.databinding.AdaptersFragmentBinding
import com.hold1.pagertabsindicator.PagerTabsIndicator

/**
 * Created by Cristian Holdunu on 08/11/2017.
 */
class AdaptersFragment : Fragment(), FragmentPresenter {
    private var tabsIndicator: PagerTabsIndicator? = null

    private lateinit var binding: AdaptersFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = AdaptersFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabsIndicator = (activity as? TabbedActivity)?.tabsIndicator
        binding.tabsText.setOnClickListener { (activity as? TabbedActivity)?.changeTabAdapter(TabbedActivity.TabAdapterType.TEXT) }
        binding.tabsImage.setOnClickListener { (activity as? TabbedActivity)?.changeTabAdapter(TabbedActivity.TabAdapterType.IMAGE) }
        binding.tabsWeb.setOnClickListener { (activity as? TabbedActivity)?.changeTabAdapter(TabbedActivity.TabAdapterType.WEB) }
        binding.tabsCustom.setOnClickListener { (activity as? TabbedActivity)?.changeTabAdapter(TabbedActivity.TabAdapterType.CUSTOM) }
        binding.tabsCustomAnim.setOnClickListener { (activity as? TabbedActivity)?.changeTabAdapter(TabbedActivity.TabAdapterType.CUSTOM_ANIM) }
    }

    override val tabName: String = "Adapters"
    override val tabImage: Int = R.drawable.ic_adapter
    override val tabImageUrl: String? = null
}