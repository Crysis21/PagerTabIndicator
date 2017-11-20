package com.hold1.pagertabsdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.hold1.pagertabsindicator.TabView;

/**
 * Created by Cristian Holdunu on 17/11/2017.
 */

public class TabItemView extends TabView {
    private final TextView tabTitle;
    private final ImageView tabIcon;
    private int activeColor;
    private int tabColor;

    public TabItemView(Context context, String title, int imgRes, int tabColor, int activeColor) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.tab_item, this);
        tabTitle = findViewById(R.id.tab_name);
        tabIcon = findViewById(R.id.tab_icon);
        tabTitle.setText(title);

        tabIcon.setImageResource(imgRes);

        this.tabColor = tabColor;
        this.activeColor = activeColor;

        this.tabIcon.setColorFilter(tabColor);
        this.tabTitle.setTextColor(tabColor);
    }

    @Override
    public void onOffset(float offset) {
        this.tabIcon.setColorFilter(Util.getColorWithOpacity(activeColor, (int) (100 * offset)));
        this.tabTitle.setTextColor(Util.mixTwoColors(activeColor,tabColor , offset));
    }
}
