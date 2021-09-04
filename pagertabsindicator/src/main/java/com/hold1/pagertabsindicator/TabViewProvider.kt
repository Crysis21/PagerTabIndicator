package com.hold1.pagertabsindicator

import android.net.Uri
import android.view.View

sealed class TabViewProvider() {

    interface ImageResource {
        fun getTabIconUri(position: Int): Uri?
        fun getTabIconResId(position: Int): Int
    }

    interface ViewResource {
        fun getTabView(position: Int): View
    }

    interface TextResource {
        fun getTabTitle(position: Int): String
    }
}