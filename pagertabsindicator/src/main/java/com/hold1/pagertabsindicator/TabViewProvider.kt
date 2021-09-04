package com.hold1.pagertabsindicator

import android.net.Uri
import android.view.View

sealed class TabViewProvider() {

    interface ImageProvider {
        fun getImageUri(position: Int): Uri?
        fun getImageResourceId(position: Int): Int
    }

    interface CustomView {
        fun getView(position: Int): View
    }
}