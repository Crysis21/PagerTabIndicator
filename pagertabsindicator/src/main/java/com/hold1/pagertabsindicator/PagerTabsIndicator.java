package com.hold1.pagertabsindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

    //CONSTANTS
    private static final String TAG = PagerTabsIndicator.class.getSimpleName();
    private static final int TAB_INDICATOR_TOP = 0;
    private static final int TAB_INDICATOR_BOTTOM = 1;
    private static final int TAB_INDICATOR_BACKGROUND = 2;
    private static final int TAB_INDICATOR_CUSTOM = 3;

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
    //Use this if you want a custom resource to be drawn as tab indicator
    private Paint bgPaing;
    private int indicatorResource = 0;
    private Paint tintPaint;
    private Paint dividerPaint;

    /**
     * Tab Divider
     */
    private boolean showDivider = true;
    private int dividerWidth = 2;
    private int dividerMargin = 10;
    private int dividerColor = Color.BLACK;
    // Use this if you want a custom resource drawn in tab divider
    private int dividerResource = -1;


    private int textColor;
    private int tabPadding;
    private boolean lockExpanded = false;

    //ViePager data
    private int position = 0;
    private int targetPosition = -1;
    private float positionOffset = 0;
    private int lastScrollX = 0;

    private int tabWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private Runnable runnable;

    private RectF bgRect = new RectF();
    private RectF indicatorRect = new RectF();

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
        indicatorResource = typedArray.getResourceId(R.styleable.PagerTabsIndicator_tab_indicator_resource, 0);
        indicatorHeight = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_indicator_height, indicatorHeight);
        indicatorMargin = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_indicator_margin, indicatorMargin);
        indicatorBgHeight = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_indicator_bg_height, indicatorBgHeight);
        indicatorColor = typedArray.getColor(R.styleable.PagerTabsIndicator_tab_indicator_color, indicatorColor);
        indicatorBgColor = typedArray.getColor(R.styleable.PagerTabsIndicator_tab_indicator_bg_color, indicatorBgColor);
        dividerWidth = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_divider_width, dividerWidth);
        dividerMargin = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_divider_margin, dividerMargin);
        dividerColor = typedArray.getColor(R.styleable.PagerTabsIndicator_tab_divider_color, dividerColor);
        dividerResource = typedArray.getResourceId(R.styleable.PagerTabsIndicator_tab_divider_resource, dividerResource);

        typedArray.recycle();

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
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        final int childCount = tabsContainer.getChildCount();
        if (lockExpanded) {
            int newTabWidth = newWidth / childCount;
            if (newTabWidth == 0 || newTabWidth == tabWidth) return;
            tabWidth = newTabWidth;
            for (int i = 0; i < tabsContainer.getChildCount(); i++) {
                View view = tabsContainer.getChildAt(i);
                view.getLayoutParams().width = tabWidth;
            }
        }
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        switch (indicatorType) {
            case TAB_INDICATOR_TOP:
                lp.topMargin = indicatorBgHeight;
                break;
            case TAB_INDICATOR_BOTTOM:
                lp.bottomMargin = indicatorBgHeight;
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
            View tabView;
            if (viewPager.getAdapter() instanceof TabViewProvider.CustomView) {
                tabView = ((TabViewProvider.CustomView) viewPager.getAdapter()).getView(i);
            } else if (viewPager.getAdapter() instanceof TabViewProvider.ImageProvider) {
                TabViewProvider.ImageProvider imageProvider = (TabViewProvider.ImageProvider) viewPager.getAdapter();
                Uri imageUri = imageProvider.getImageUri(i);
                if (imageUri == null) {
                    String resourceScheme = "res";
                    imageUri = new Uri.Builder()
                            .scheme(resourceScheme)
                            .path(String.valueOf(imageProvider.getImageUri(i)))
                            .build();
                }
                tabView = createImageView(imageUri);
            } else {
                tabView = createTextView(viewPager.getAdapter().getPageTitle(i).toString());
            }
            addTabView(tabView, i);
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

    private void addTabView(View view, final int position) {
        tabsContainer.addView(view, new LinearLayout.LayoutParams(tabWidth, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setPadding(50,0,50,0);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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

        bgRect.left = 0;
        bgRect.right = tabsContainer.getRight();

        View currentTab = tabsContainer.getChildAt(position);
        indicatorRect.left = currentTab.getLeft();
        indicatorRect.right = currentTab.getRight();

        if (positionOffset > 0f && position < viewPager.getAdapter().getCount() - 1) {
            View nextTab = tabsContainer.getChildAt(position + 1);
            indicatorRect.left = (positionOffset * nextTab.getLeft() + (1f - positionOffset) * indicatorRect.left);
            indicatorRect.right = (positionOffset * nextTab.getRight() + (1f - positionOffset) * indicatorRect.right);
        }

        //draw slider
        switch (indicatorType) {
            case TAB_INDICATOR_TOP:
                bgRect.top = 0;
                bgRect.bottom = indicatorHeight;
                indicatorRect.top = indicatorMargin;
                indicatorRect.bottom = indicatorHeight + indicatorMargin;
                break;
            case TAB_INDICATOR_BOTTOM:
            default:
                bgRect.top = getHeight() - indicatorHeight;
                bgRect.bottom = getHeight();
                indicatorRect.top = getHeight() - indicatorHeight - indicatorMargin;
                indicatorRect.bottom = getHeight() - indicatorMargin;
        }

        canvas.drawRect(bgRect, bgPaing);
        canvas.drawRect(indicatorRect, tintPaint);

        //draw divider
        for (int i = 0; i < tabsContainer.getChildCount() - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            float startX = tab.getRight() - dividerWidth / 2;
            float endX = startX + dividerWidth;
            float startY = dividerMargin;
            float endY = getHeight() - indicatorBgHeight - dividerMargin;
            canvas.drawRect(startX, startY, endX, endY, dividerPaint);
        }
    }

    //Listen View Pager events
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d(TAG, "position=" + position + " positionOffset=" + positionOffset + "  pixels=" + positionOffsetPixels);
        this.position = position;
        this.positionOffset = positionOffset;
        if (targetPosition == -1)
            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));
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
}
