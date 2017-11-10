package com.hold1.pagertabsindicator;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Cristian Holdunu on 09/11/2017.
 */

public class TabView extends FrameLayout{

    public TabView(Context context) {
        super(context);
    }

    public TabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelected(boolean selected) {
        setBackgroundColor(Color.WHITE);
    }
}
