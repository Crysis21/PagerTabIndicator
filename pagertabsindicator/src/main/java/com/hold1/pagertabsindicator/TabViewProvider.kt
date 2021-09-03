package com.hold1.pagertabsindicator

import android.net.Uri
import android.view.View
import android.view.ViewParent
import androidx.viewpager.widget.ViewPager

/**
 * Created by Cristian Holdunu on 08/11/2017.
 */
class TabViewProvider() {
    private var viewPager: ViewPager? = null
    private var callback: TabViewProviderCallback? = null

    constructor(viewPager: ViewPager) : this() {

    }

    interface TabViewProviderCallback {
        fun invalidateItems()
    }

    interface ImageProvider {
        fun getImageUri(position: Int): Uri?
        fun getImageResourceId(position: Int): Int
    }

    interface CustomView {
        fun getView(position: Int): View?
    }

}