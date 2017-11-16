package com.hold1.pagertabsindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
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

    //CONSTANTS
    private static final String TAG = PagerTabsIndicator.class.getSimpleName();
    public static final int TAB_INDICATOR_TOP = 0;
    public static final int TAB_INDICATOR_BOTTOM = 1;
    public static final int TAB_INDICATOR_BACKGROUND = 2;

    public static final int SCALE_FIT_XY = 0;
    public static final int SCALE_CENTER_INSIDE = 1;

    private LinearLayout tabsContainer;
    private ViewPager viewPager;

    private int textSize;

    /**
     * Tab Indicator
     */

    private int indicatorType = TAB_INDICATOR_BOTTOM;
    private int indicatorHeight = 20;
    private int indicatorBgHeight = 20;
    private int indicatorMargin = 0;
    private int indicatorColor;
    private int indicatorBgColor;
    private Drawable indicatorDrawable;
    //Use this if you want a custom resource to be drawn as tab indicator
    private Paint bgPaing;
    private int indicatorResource = -1;
    private int indicatorScaleType = SCALE_CENTER_INSIDE;
    private Paint tintPaint;
    private Paint dividerPaint;

    /**
     * Tab Divider
     */
    private boolean showDivider = true;
    private int dividerWidth = 2;
    private int dividerMargin = 10;
    private int tabElevation = 6;
    private int dividerColor = Color.BLACK;
    // Use this if you want a custom resource drawn in tab divider
    private int dividerResource = -1;
    private Drawable dividerDrawable;


    private int textColor;
    private int tabPadding;
    private boolean lockExpanded = false;
    private boolean showBarIndicator = true;
    private boolean disableTabAnimation = false;

    //ViePager data
    private int position = 0;
    private int oldPosition = 0;
    private int targetPosition = -1;
    private float positionOffset = 0;
    private int lastScrollX = 0;

    private int tabWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private Runnable runnable;

    private RectF bgRect = new RectF();
    private Rect indicatorRect = new Rect();

    public PagerTabsIndicator(Context context) {
        super(context, null);
    }

    public PagerTabsIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerTabsIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setHorizontalScrollBarEnabled(false);
        tabsContainer = new LinearLayout(getContext());
        addView(tabsContainer, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        //Load default dimens
        textSize = getContext().getResources().getDimensionPixelSize(R.dimen.tab_default_text_size);
        indicatorHeight = getContext().getResources().getDimensionPixelSize(R.dimen.tab_default_indicator_height);
        indicatorBgHeight = getContext().getResources().getDimensionPixelSize(R.dimen.tab_default_indicator_bg_height);
        dividerWidth = getContext().getResources().getDimensionPixelSize(R.dimen.tab_default_divider_width);
        dividerMargin = getContext().getResources().getDimensionPixelSize(R.dimen.tab_default_divider_margin);

        //Load default colors
        textColor = getResources().getColor(R.color.tab_default_text_color);
        indicatorColor = getResources().getColor(R.color.tab_indicator_color);
        indicatorBgColor = getResources().getColor(R.color.tab_indicator_bg_color);
        dividerColor = getResources().getColor(R.color.tab_default_divider_color);
        tabPadding = getResources().getDimensionPixelSize(R.dimen.tab_default_padding);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerTabsIndicator);

        tabPadding = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_padding, tabPadding);
        textSize = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_text_size, textSize);
        textColor = typedArray.getColor(R.styleable.PagerTabsIndicator_tab_text_color, textColor);
        showDivider = typedArray.getBoolean(R.styleable.PagerTabsIndicator_tab_show_divider, showDivider);
        lockExpanded = typedArray.getBoolean(R.styleable.PagerTabsIndicator_tab_lock_expanded, lockExpanded);
        indicatorType = typedArray.getInt(R.styleable.PagerTabsIndicator_tab_indicator, indicatorType);
        indicatorResource = typedArray.getResourceId(R.styleable.PagerTabsIndicator_tab_indicator_resource, indicatorResource);
        indicatorHeight = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_indicator_height, indicatorHeight);
        indicatorBgHeight = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_indicator_bg_height, indicatorBgHeight);
        indicatorMargin = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_indicator_margin, indicatorMargin);
        indicatorColor = typedArray.getColor(R.styleable.PagerTabsIndicator_tab_indicator_color, indicatorColor);
        indicatorBgColor = typedArray.getColor(R.styleable.PagerTabsIndicator_tab_indicator_bg_color, indicatorBgColor);
        dividerWidth = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_divider_width, dividerWidth);
        dividerMargin = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_divider_margin, dividerMargin);
        dividerColor = typedArray.getColor(R.styleable.PagerTabsIndicator_tab_divider_color, dividerColor);
        dividerResource = typedArray.getResourceId(R.styleable.PagerTabsIndicator_tab_divider_resource, dividerResource);
        showBarIndicator = typedArray.getBoolean(R.styleable.PagerTabsIndicator_tab_show_bar_indicator, showBarIndicator);
        tabElevation = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_elevation, tabElevation);
        indicatorScaleType = typedArray.getInt(R.styleable.PagerTabsIndicator_tab_indicator_scale_type, indicatorScaleType);
        disableTabAnimation = typedArray.getBoolean(R.styleable.PagerTabsIndicator_tab_disable_animation, disableTabAnimation);

        typedArray.recycle();
        prepareResources();
    }

    private void prepareResources(){
        bgPaing = new Paint();
        bgPaing.setColor(indicatorBgColor);
        bgPaing.setStyle(Paint.Style.FILL);
        bgPaing.setAntiAlias(true);

        tintPaint = new Paint();
        tintPaint.setColor(indicatorColor);
        tintPaint.setStyle(Paint.Style.FILL);
        tintPaint.setAntiAlias(true);

        dividerPaint = new Paint();
        dividerPaint.setColor(dividerColor);
        dividerPaint.setStyle(Paint.Style.FILL);
        dividerPaint.setAntiAlias(true);

        if (indicatorResource != -1) {
            indicatorDrawable = getResources().getDrawable(indicatorResource);
        }
        if (dividerResource != -1) {
            dividerDrawable = getResources().getDrawable(dividerResource);
        }

        ViewCompat.setElevation(this, tabElevation);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d(TAG, "layout width=" + (r - l));
        final int newWidth = r - l;
        final int childCount = tabsContainer.getChildCount();
        if (lockExpanded) {
            int newTabWidth = newWidth / childCount;
            if (newTabWidth == 0 || newTabWidth == tabWidth) return;
            tabWidth = newTabWidth;
            Log.d(TAG, "newWidth=" + newWidth + " tabWidth=" + tabWidth);
            for (int i = 0; i < tabsContainer.getChildCount(); i++) {
                View view = tabsContainer.getChildAt(i);
                view.getLayoutParams().width = tabWidth;
                view.requestLayout();
            }
        }
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        if (showBarIndicator)
            switch (indicatorType) {
                case TAB_INDICATOR_TOP:
                    lp.topMargin = indicatorBgHeight;
                    lp.bottomMargin = 0;
                    break;
                case TAB_INDICATOR_BOTTOM:
                    lp.bottomMargin = indicatorBgHeight;
                    lp.topMargin = 0;
                    break;
            }

        super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
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
            View innerView;
            if (viewPager.getAdapter() instanceof TabViewProvider.CustomView) {
                innerView = ((TabViewProvider.CustomView) viewPager.getAdapter()).getView(i);
            } else if (viewPager.getAdapter() instanceof TabViewProvider.ImageProvider) {
                TabViewProvider.ImageProvider imageProvider = (TabViewProvider.ImageProvider) viewPager.getAdapter();
                innerView = createImageView();
                if (imageProvider.getImageUri(i) != null) {
                    Glide.with(getContext()).load(imageProvider.getImageUri(i)).into((ImageView) innerView);
                } else if (imageProvider.getImageResourceId(i) != 0) {
                    ((ImageView) innerView).setImageResource(imageProvider.getImageResourceId(i));
                }
                innerView = new TabView(getContext(), innerView);
            } else {
                innerView = createTextView(viewPager.getAdapter().getPageTitle(i).toString());
                innerView = new TabView(getContext(), innerView);
            }

            addTabView(innerView, i);
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

    private View createImageView() {
        ImageView imageView = new ImageView(getContext());
        imageView.setPadding(10,10,10,10);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        return imageView;
    }

    private void addTabView(View view, final int position) {
        tabsContainer.addView(view, new LinearLayout.LayoutParams(tabWidth, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setPadding(tabPadding, 0, tabPadding, 0);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "tab click " + position);
                viewPager.setCurrentItem(position);
                targetPosition = position;
            }
        });
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
        drawDivider(canvas);
    }

    private void drawIndicator(Canvas canvas) {
        if (!showBarIndicator) return;

        bgRect.left = 0;
        bgRect.right = tabsContainer.getRight();

        View currentTab = tabsContainer.getChildAt(position);
        if (disableTabAnimation) {
            currentTab = tabsContainer.getChildAt(Math.round(position + positionOffset));
        }

        indicatorRect.left = currentTab.getLeft();
        indicatorRect.right = currentTab.getRight();

        if (positionOffset > 0f && position < viewPager.getAdapter().getCount() - 1 && !disableTabAnimation) {
            View nextTab = tabsContainer.getChildAt(position + 1);
            indicatorRect.left = (int) (positionOffset * nextTab.getLeft() + (1f - positionOffset) * indicatorRect.left);
            indicatorRect.right = (int) (positionOffset * nextTab.getRight() + (1f - positionOffset) * indicatorRect.right);
        }

        //draw slider
        switch (indicatorType) {
            case TAB_INDICATOR_TOP:
                bgRect.top = 0;
                bgRect.bottom = indicatorBgHeight;
                indicatorRect.top = indicatorMargin;
                indicatorRect.bottom = indicatorHeight + indicatorMargin;
                break;
            case TAB_INDICATOR_BOTTOM:
            default:
                bgRect.top = getHeight() - indicatorBgHeight;
                bgRect.bottom = getHeight();
                indicatorRect.top = getHeight() - indicatorHeight - indicatorMargin;
                indicatorRect.bottom = getHeight() - indicatorMargin;
        }

        canvas.drawRect(bgRect, bgPaing);

        if (indicatorDrawable == null)
            canvas.drawRect(indicatorRect, tintPaint);
        else {
            if (indicatorScaleType == SCALE_CENTER_INSIDE) {
                //Adjust the indicator bounds aspect ratio to keep the indicator resource intact
                float ratio = indicatorDrawable.getIntrinsicHeight() / indicatorDrawable.getIntrinsicWidth();
                float scaledWidth = indicatorHeight * ratio;
                indicatorRect.left = (int) (indicatorRect.centerX() - scaledWidth / 2);
                indicatorRect.right = (int) (indicatorRect.left + scaledWidth);
            }
            indicatorDrawable.setBounds(indicatorRect);
            indicatorDrawable.draw(canvas);
        }
    }

    private void drawDivider(Canvas canvas) {
        if (!showDivider) return;
        for (int i = 0; i < tabsContainer.getChildCount() - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            int startX = tab.getRight() - dividerWidth / 2;
            int endX = startX + dividerWidth;
            int startY = 0;
            int endY = getHeight();
            if (indicatorType == TAB_INDICATOR_BOTTOM) {
                startY = dividerMargin;
                endY = getHeight() - indicatorBgHeight - dividerMargin;
            } else if (indicatorType == TAB_INDICATOR_TOP) {
                startY = indicatorBgHeight + dividerMargin;
                endY = getHeight() - dividerMargin;
            }
            if (dividerDrawable == null)
                canvas.drawRect(startX, startY, endX, endY, dividerPaint);
            else {
                dividerDrawable.setBounds(new Rect(startX, startY, endX, endY));
                dividerDrawable.draw(canvas);
            }
        }
    }

    //Listen View Pager events
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d(TAG, "position=" + position + " offset=" + positionOffset);
        this.position = position;
        this.positionOffset = positionOffset;
        if (targetPosition == -1)
            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));
        int targetPosition = Math.round(position + positionOffset);
        float targetOffset = (float) Math.abs(0.5 - positionOffset) * 2;
        for (int i = 0; i < tabsContainer.getChildCount(); i++) {
            View child = tabsContainer.getChildAt(i);
            if (!(child instanceof TabView)) continue;
            if (i == targetPosition) {
                ((TabView) child).onOffset(targetOffset);
            } else {
                ((TabView) child).onOffset(0);
            }
        }
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected=" + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (targetPosition != -1)
                setTabSelected(targetPosition);
            targetPosition = -1;
        }
    }

    public void setTabSelected(int position) {
        this.position = position;
        final int tabCount = tabsContainer.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = tabsContainer.getChildAt(i);
            final boolean isSelected = i == position;
            if (indicatorType == TAB_INDICATOR_BACKGROUND)
                child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(position);
            }
        }
    }

    public void animateToTab(final int position) {
        final View tabView = tabsContainer.getChildAt(position);
        if (runnable != null) {
            removeCallbacks(runnable);
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                runnable = null;
            }
        };
        post(runnable);
    }

    private void scrollToChild(int position, int offset) {
        View tabView = tabsContainer.getChildAt(position);
        int newScrollX = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2 + offset;
        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }


    //Getters and setters that should behave like a builder pattern

    public int getTextSize() {
        return textSize;
    }

    public PagerTabsIndicator setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    public int getIndicatorType() {
        return indicatorType;
    }

    public PagerTabsIndicator setIndicatorType(int indicatorType) {
        this.indicatorType = indicatorType;
        return this;
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public PagerTabsIndicator setIndicatorHeight(int indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
        return this;
    }

    public int getIndicatorBgHeight() {
        return indicatorBgHeight;
    }

    public PagerTabsIndicator setIndicatorBgHeight(int indicatorBgHeight) {
        this.indicatorBgHeight = indicatorBgHeight;
        return this;
    }

    public int getIndicatorMargin() {
        return indicatorMargin;
    }

    public PagerTabsIndicator setIndicatorMargin(int indicatorMargin) {
        this.indicatorMargin = indicatorMargin;
        return this;
    }

    public int getIndicatorColor() {
        return indicatorColor;
    }

    public PagerTabsIndicator setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        return this;
    }

    public int getIndicatorBgColor() {
        return indicatorBgColor;
    }

    public PagerTabsIndicator setIndicatorBgColor(int indicatorBgColor) {
        this.indicatorBgColor = indicatorBgColor;
        return this;
    }

    public boolean isShowDivider() {
        return showDivider;
    }

    public PagerTabsIndicator setShowDivider(boolean showDivider) {
        this.showDivider = showDivider;
        return this;
    }

    public int getDividerWidth() {
        return dividerWidth;
    }

    public PagerTabsIndicator setDividerWidth(int dividerWidth) {
        this.dividerWidth = dividerWidth;
        return this;
    }

    public int getDividerMargin() {
        return dividerMargin;
    }

    public PagerTabsIndicator setDividerMargin(int dividerMargin) {
        this.dividerMargin = dividerMargin;
        return this;
    }

    public int getTabElevation() {
        return tabElevation;
    }

    public PagerTabsIndicator setTabElevation(int tabElevation) {
        this.tabElevation = tabElevation;
        return this;
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public PagerTabsIndicator setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        return this;
    }

    public int getTextColor() {
        return textColor;
    }

    public PagerTabsIndicator setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public int getTabPadding() {
        return tabPadding;
    }

    public PagerTabsIndicator setTabPadding(int tabPadding) {
        this.tabPadding = tabPadding;
        return this;
    }

    public boolean isLockExpanded() {
        return lockExpanded;
    }

    public PagerTabsIndicator setLockExpanded(boolean lockExpanded) {
        this.lockExpanded = lockExpanded;
        return this;
    }

    public boolean isShowBarIndicator() {
        return showBarIndicator;
    }

    public PagerTabsIndicator setShowBarIndicator(boolean showBarIndicator) {
        this.showBarIndicator = showBarIndicator;
        return this;
    }

    public boolean isDisableTabAnimation() {
        return disableTabAnimation;
    }

    public PagerTabsIndicator setDisableTabAnimation(boolean disableTabAnimation) {
        this.disableTabAnimation = disableTabAnimation;
        return this;
    }

    public void refresh(){
        tabWidth = LayoutParams.WRAP_CONTENT;
        prepareResources();
        notifyDatasetChanged();
    }
}
