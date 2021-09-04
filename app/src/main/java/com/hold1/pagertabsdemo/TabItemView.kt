package com.hold1.pagertabsdemo

import android.content.Context
import com.hold1.pagertabsdemo.Util.getColorWithOpacity
import com.hold1.pagertabsdemo.Util.mixTwoColors
import android.widget.TextView
import android.view.LayoutInflater
import android.widget.ImageView
import com.hold1.pagertabsdemo.R
import com.hold1.pagertabsindicator.TabView

/**
 * Created by Cristian Holdunu on 17/11/2017.
 */
class TabItemView(context: Context, title: String?, imgRes: Int, tabColor: Int, activeColor: Int) : TabView(context) {
    private val tabTitle: TextView
    private val tabIcon: ImageView
    private val activeColor: Int
    private val tabColor: Int
    override fun onOffset(offset: Float) {
        tabIcon.setColorFilter(getColorWithOpacity(activeColor, (100 * offset).toInt()))
        tabTitle.setTextColor(mixTwoColors(activeColor, tabColor, offset))
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.tab_item, this)
        tabTitle = findViewById(R.id.tab_name)
        tabIcon = findViewById(R.id.tab_icon)
        tabTitle.text = title
        tabIcon.setImageResource(imgRes)
        this.tabColor = tabColor
        this.activeColor = activeColor
        tabIcon.setColorFilter(tabColor)
        tabTitle.setTextColor(tabColor)
    }
}