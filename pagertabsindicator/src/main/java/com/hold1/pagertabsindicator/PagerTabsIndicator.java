package com.hold1.pagertabsindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by Cristian Holdunu on 08/11/2017.
 */

public class PagerTabsIndicator extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
    private static final String TAG = PagerTabsIndicator.class.getSimpleName();

    private LinearLayout tabsContainer;
    private ViewPager viewPager;
    private int textColor;
    private float textSize;
    private boolean fixedSize;

    public PagerTabsIndicator(Context context) {
        super(context, null);
    }

    public PagerTabsIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerTabsIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        tabsContainer = new LinearLayout(getContext());
        addView(tabsContainer, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerTabsIndicator);

        textSize = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_text_size, getContext().getResources().getDimensionPixelSize(R.dimen.tab_default_text_size));
        textColor = typedArray.getColor(R.styleable.PagerTabsIndicator_tab_text_color, Color.BLACK);
        fixedSize = typedArray.getBoolean(R.styleable.PagerTabsIndicator_tab_fixed_size, false);

        typedArray.recycle();

    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        viewPager.addOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener() {
            @Override
            public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
                listenToAdapterChanges(newAdapter);
            }
        });

        listenToAdapterChanges(viewPager.getAdapter());
    }

    public void notifyDatasetChanged() {
        if (viewPager == null || viewPager.getAdapter() == null) return;
        tabsContainer.removeAllViews();

        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
            View tabView;
            if (viewPager.getAdapter() instanceof TabViewProvider.CustomView) {
                tabView = ((TabViewProvider.CustomView) viewPager.getAdapter()).getView(i);
            } else if (viewPager.getAdapter() instanceof TabViewProvider.ImageProvider) {
                tabView = createImageView(((TabViewProvider.ImageProvider) viewPager.getAdapter()).getImageUri(i));
            } else {
                tabView = createTextView(viewPager.getAdapter().getPageTitle(i).toString());
            }
            addTabView(tabView);
        }
    }

    private View createTextView(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setSingleLine();
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setTextColor(textColor);
        return textView;
    }

    private View createImageView(Uri imageUri) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageURI(imageUri);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(getContext()).load(imageUri).into(imageView);
        return imageView;
    }

    private void addTabView(View view) {
        tabsContainer.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void listenToAdapterChanges(PagerAdapter pagerAdapter) {
        if (pagerAdapter == null) {
            Log.e(TAG, "listenToAdapterChanges - pager adapter is null. can't register");
            return;
        }

        pagerAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                notifyDatasetChanged();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
            }
        });

        notifyDatasetChanged();
    }

    //Listen View Pager events
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
