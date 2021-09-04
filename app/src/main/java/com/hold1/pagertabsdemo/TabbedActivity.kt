package com.hold1.pagertabsdemo

import com.hold1.pagertabsindicator.PagerTabsIndicator

interface TabbedActivity {

    enum class TabAdapterType {
        TEXT, IMAGE, WEB, CUSTOM, CUSTOM_ANIM
    }

    val tabsIndicator: PagerTabsIndicator

    fun changeTabAdapter(adapterType: TabAdapterType?)

    fun addDummyTab()

}