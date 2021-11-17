package com.hold1.pagertabsindicator.extensions

import android.view.View


fun View.getCenterX(): Int {
    return right - measuredWidth / 2
}