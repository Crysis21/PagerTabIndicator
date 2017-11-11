package com.hold1.pagertabsindicator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Cristian Holdunu on 09/11/2017.
 */

public class TabView extends FrameLayout {

    private ValueAnimator bgAnimator;
    private int pressColor = Color.RED;
    private int currentBgColor = pressColor;

    public TabView(Context context) {
        super(context);
        bgAnimator = ValueAnimator.ofInt(100, 0);
        bgAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setBackgroundColor(Color.argb((Integer) animation.getAnimatedValue(), Color.red(pressColor), Color.green(pressColor), Color.blue(pressColor)));
            }
        });
    }

    @Override
    public void setSelected(boolean selected) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(pressColor);
                break;
                case MotionEvent.ACTION_UP:
                    bgAnimator.start();
                    break;
        }
        return super.onTouchEvent(event);

    }
}
