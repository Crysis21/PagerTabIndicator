package com.hold1.pagertabsindicator

import android.net.Uri
import android.view.View

sealed interface TabViewProvider {

    interface ImageResource: TabViewProvider {
        fun getTabIconUri(position: Int): Uri?
        fun getTabIconResId(position: Int): Int
    }

    interface ViewResource: TabViewProvider {
        fun getTabView(position: Int): View
    }

    interface TextResource: TabViewProvider {
        fun getTabTitle(position: Int): String
    }
}