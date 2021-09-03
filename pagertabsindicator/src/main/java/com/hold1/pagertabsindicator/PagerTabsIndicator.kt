package com.hold1.pagertabsindicator

import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RestrictTo
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnAdapterChangeListener
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bumptech.glide.Glide
import com.hold1.pagertabsindicator.TabViewProvider.CustomView
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Created by Cristian Holdunu on 08/11/2017.
 */

class PagerTabsIndicator @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null
) : HorizontalScrollView(context, attrs) {

    interface OnItemSelectedListener {
        fun onItemSelected(position: Int)
    }

    interface OnItemReselectedListener {
        fun onItemReselected(position: Int)
    }

    var onItemSelectedListener: OnItemSelectedListener? = null
    var onItemReselectedListener: OnItemReselectedListener? = null
    private var adapter: TabsAdapter? = null

    private var tabsContainer: LinearLayout = LinearLayout(context)
    private var indicatorType = TAB_INDICATOR_BOTTOM
    private var indicatorHeight = resources.getDimensionPixelSize(R.dimen.tab_default_indicator_height)
    private var indicatorBgHeight = resources.getDimensionPixelSize(R.dimen.tab_default_indicator_bg_height)
    private var indicatorMargin = 0
    private var indicatorColor = resources.getColor(R.color.tab_indicator_color)
    private var indicatorBgColor = resources.getColor(R.color.tab_indicator_bg_color)
    private var indicatorDrawable: Drawable? = null
    private var indicatorResource = -1
        @SuppressLint("UseCompatLoadingForDrawables")
        set(value) {
            field = value
            indicatorDrawable = if (value == -1) {
                null
            } else {
                resources.getDrawable(value)
            }
        }
    private var indicatorScaleType = SCALE_CENTER_INSIDE

    private var backgroundPaint: Paint = Paint().apply {
        this.color = indicatorBgColor
        this.style = Paint.Style.FILL
        this.isAntiAlias = true
    }
    private var tintPaint: Paint = Paint().apply {
        this.color = indicatorColor
        this.style = Paint.Style.FILL
        this.isAntiAlias = true
    }
    private var dividerPaint: Paint = Paint().apply {
        this.color = dividerColor
        this.style = Paint.Style.FILL
        this.isAntiAlias = true
    }

    private var isShowDivider = true
    private var dividerWidth = resources.getDimensionPixelSize(R.dimen.tab_default_divider_width)
    private var dividerMargin = resources.getDimensionPixelSize(R.dimen.tab_default_divider_margin)
    private var tabElevation = 0
        set(value) {
            field = value
            ViewCompat.setElevation(this, value.toFloat())
        }
    private var dividerColor = resources.getColor(R.color.tab_default_divider_color)

    // Use this if you want a custom resource drawn in tab divider
    private var dividerResource = -1
    private var dividerDrawable: Drawable? = null

    private var isHighlightText = false

    private var textSize = resources.getDimensionPixelSize(R.dimen.tab_default_text_size)
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    private var textColor = resources.getColor(R.color.tab_default_text_color)

    private var highlightTextColor = resources.getColor(R.color.tab_indicator_color)
    private var tabPadding = resources.getDimensionPixelSize(R.dimen.tab_default_padding)
    private var isLockExpanded = false
    private var showBarIndicator = true
    private var isDisableTabAnimation = false

    //ViewPager data
    private var position = 0
    private val oldPosition = 0
    private var targetPosition = -1
    private var positionOffset = 0f
    private var lastScrollX = 0
    private var tabWidth = ViewGroup.LayoutParams.WRAP_CONTENT
    private var runnable: Runnable? = null
    private val bgRect = RectF()
    private val indicatorRect = Rect()

    init {
        isHorizontalScrollBarEnabled = false
        addView(tabsContainer, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerTabsIndicator)
        tabPadding = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_padding, tabPadding)
        textSize = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_text_size, textSize)
        textColor = typedArray.getColor(R.styleable.PagerTabsIndicator_tab_text_color, textColor)
        highlightTextColor = typedArray.getColor(R.styleable.PagerTabsIndicator_tab_highlight_text_color, highlightTextColor)
        isHighlightText = typedArray.getBoolean(R.styleable.PagerTabsIndicator_tab_highlight_text_color, isHighlightText)
        isShowDivider = typedArray.getBoolean(R.styleable.PagerTabsIndicator_tab_show_divider, isShowDivider)
        isLockExpanded = typedArray.getBoolean(R.styleable.PagerTabsIndicator_tab_lock_expanded, isLockExpanded)
        indicatorType = typedArray.getInt(R.styleable.PagerTabsIndicator_tab_indicator, indicatorType)
        indicatorResource = typedArray.getResourceId(R.styleable.PagerTabsIndicator_tab_indicator_resource, indicatorResource)
        indicatorHeight = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_indicator_height, indicatorHeight)
        indicatorBgHeight = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_indicator_bg_height, indicatorBgHeight)
        indicatorMargin = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_indicator_margin, indicatorMargin)
        indicatorColor = typedArray.getColor(R.styleable.PagerTabsIndicator_tab_indicator_color, indicatorColor)
        indicatorBgColor = typedArray.getColor(R.styleable.PagerTabsIndicator_tab_indicator_bg_color, indicatorBgColor)
        dividerWidth = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_divider_width, dividerWidth)
        dividerMargin = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_divider_margin, dividerMargin)
        dividerColor = typedArray.getColor(R.styleable.PagerTabsIndicator_tab_divider_color, dividerColor)
        dividerResource = typedArray.getResourceId(R.styleable.PagerTabsIndicator_tab_divider_resource, dividerResource)
        showBarIndicator = typedArray.getBoolean(R.styleable.PagerTabsIndicator_tab_show_bar_indicator, showBarIndicator)
        tabElevation = typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_elevation, tabElevation)
        indicatorScaleType = typedArray.getInt(R.styleable.PagerTabsIndicator_tab_indicator_scale_type, indicatorScaleType)
        isDisableTabAnimation = typedArray.getBoolean(R.styleable.PagerTabsIndicator_tab_disable_animation, isDisableTabAnimation)
        typedArray.recycle()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return !isLockExpanded && super.onInterceptTouchEvent(ev)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        Log.d(TAG, "layout width=" + (r - l))
        if (!changed) return
        val newWidth = r - l
        val childCount = tabsContainer.childCount
        if (isLockExpanded && childCount > 0) {
            val newTabWidth = newWidth / childCount
            if (newTabWidth == 0) return
            tabWidth = newTabWidth
            Log.d(TAG, "newWidth=$newWidth tabWidth=$tabWidth")
            for (i in 0 until tabsContainer.childCount) {
                val view = tabsContainer.getChildAt(i)
                view.layoutParams.width = tabWidth
            }
            tabsContainer.layoutParams = tabsContainer.layoutParams
            tabsContainer.requestLayout()
        }
    }

    override fun measureChildWithMargins(
            child: View,
            parentWidthMeasureSpec: Int,
            widthUsed: Int,
            parentHeightMeasureSpec: Int,
            heightUsed: Int
    ) {
        val lp = child.layoutParams as MarginLayoutParams
        if (showBarIndicator) when (indicatorType) {
            TAB_INDICATOR_TOP -> {
                lp.topMargin = indicatorBgHeight
                lp.bottomMargin = 0
            }
            TAB_INDICATOR_BOTTOM -> {
                lp.bottomMargin = indicatorBgHeight
                lp.topMargin = 0
            }
        } else {
            lp.topMargin = paddingTop
            lp.bottomMargin = paddingBottom
        }
        super.measureChildWithMargins(
                child,
                parentWidthMeasureSpec,
                widthUsed,
                parentHeightMeasureSpec,
                heightUsed
        )
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun invalidateViews() {
        tabsContainer.removeAllViews()
        adapter?.let { tabsAdapter ->
            for (i in 0 until tabsAdapter.getCount()) {
                addTabView(tabsAdapter.getTabAt(i), i)
            }
        }
    }
    fun setAdapter(adapter: TabsAdapter?) {
        this.adapter = adapter
        this.adapter?.pagerTabsIndicator = this
    }

    private fun addTabView(view: View?, position: Int) {
        tabsContainer.addView(view, LinearLayout.LayoutParams(tabWidth, ViewGroup.LayoutParams.MATCH_PARENT))
        view?.setPadding(tabPadding, 0, tabPadding, 0)
        view?.setOnClickListener {
            Log.d(TAG, "tab click $position")
            onItemSelectedListener?.onItemSelected(position)
            if (this.position == position) {
                onItemReselectedListener?.onItemReselected(position)
            }
            targetPosition = position
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawIndicator(canvas)
        drawDivider(canvas)
    }

    private fun drawIndicator(canvas: Canvas) {
        if (!showBarIndicator) return
        bgRect.left = 0f
        bgRect.right = max(right, tabsContainer.width).toFloat()

        var tab: View? = tabsContainer.getChildAt(position) ?: return
        if (isDisableTabAnimation) {
            tab = tabsContainer.getChildAt((position + positionOffset).roundToInt())
        }
        tab?.let { currentTab->
            indicatorRect.left = currentTab.left
            indicatorRect.right = currentTab.right
        }
        adapter?.let {
            if (positionOffset > 0f && position < it.getCount() - 1 && !isDisableTabAnimation) {
                val nextTab = tabsContainer.getChildAt(position + 1)
                indicatorRect.left =
                        (positionOffset * nextTab.left + (1f - positionOffset) * indicatorRect.left).toInt()
                indicatorRect.right =
                        (positionOffset * nextTab.right + (1f - positionOffset) * indicatorRect.right).toInt()
            }
        }
        when (indicatorType) {
            TAB_INDICATOR_TOP -> {
                bgRect.top = 0f
                bgRect.bottom = indicatorBgHeight.toFloat()
                indicatorRect.top = indicatorMargin
                indicatorRect.bottom = indicatorHeight + indicatorMargin
            }
            TAB_INDICATOR_BOTTOM -> {
                bgRect.top = (height - indicatorBgHeight).toFloat()
                bgRect.bottom = height.toFloat()
                indicatorRect.top = height - indicatorHeight - indicatorMargin
                indicatorRect.bottom = height - indicatorMargin
            }
            else -> {
                bgRect.top = (height - indicatorBgHeight).toFloat()
                bgRect.bottom = height.toFloat()
                indicatorRect.top = height - indicatorHeight - indicatorMargin
                indicatorRect.bottom = height - indicatorMargin
            }
        }
        canvas.drawRect(bgRect, backgroundPaint)
        if (indicatorDrawable == null) canvas.drawRect(indicatorRect, tintPaint) else {
            if (indicatorScaleType == SCALE_CENTER_INSIDE) {
                //Adjust the indicator bounds aspect ratio to keep the indicator resource intact
                val ratio =
                        (indicatorDrawable!!.intrinsicHeight / indicatorDrawable!!.intrinsicWidth).toFloat()
                val scaledWidth = indicatorHeight * ratio
                indicatorRect.left = (indicatorRect.centerX() - scaledWidth / 2).toInt()
                indicatorRect.right = (indicatorRect.left + scaledWidth).toInt()
            }
            indicatorDrawable!!.bounds = indicatorRect
            indicatorDrawable!!.draw(canvas)
        }
    }

    private fun drawDivider(canvas: Canvas) {
        if (!isShowDivider) return
        for (i in 0 until tabsContainer.childCount - 1) {
            val tab = tabsContainer.getChildAt(i)
            val startX = tab.right - dividerWidth / 2
            val endX = startX + dividerWidth
            var startY = 0
            var endY = height
            if (indicatorType == TAB_INDICATOR_BOTTOM) {
                startY = dividerMargin
                endY = height - indicatorBgHeight - dividerMargin
            } else if (indicatorType == TAB_INDICATOR_TOP) {
                startY = indicatorBgHeight + dividerMargin
                endY = height - dividerMargin
            }

            dividerDrawable?.let { drawable ->
                drawable.bounds = Rect(startX, startY, endX, endY)
                drawable.draw(canvas)
            } ?: run {
                canvas.drawRect(
                        startX.toFloat(),
                        startY.toFloat(),
                        endX.toFloat(),
                        endY.toFloat(),
                        dividerPaint
                )
            }
        }
    }

    //Listen View Pager events
    fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        Log.d(
                TAG,
                "onPageScrolled:position=" + position + ";positionOffset:" + positionOffset + ";tabsContainer child count:" + tabsContainer.childCount
        )
        this.position = position
        this.positionOffset = positionOffset
        if (targetPosition == -1 && tabsContainer.childCount > position) {
            scrollToChild(
                    position,
                    (positionOffset * tabsContainer.getChildAt(position).width).toInt()
            )
        }
        val targetPosition = (position + positionOffset).roundToInt()
        val targetOffset = Math.abs(0.5 - positionOffset).toFloat() * 2
        for (i in 0 until tabsContainer.childCount) {
            val child = tabsContainer.getChildAt(i) as? TabView ?: continue
            if (i == targetPosition) {
                child.onOffset(targetOffset)
            } else {
                child.onOffset(0f)
            }
        }
        invalidate()
    }

    fun onPageSelected(position: Int) {
        Log.d(TAG, "onPageSelected=$position")
    }

    fun onPageScrollStateChanged(state: Int) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (targetPosition != -1) setTabSelected(targetPosition)
            targetPosition = -1
        }
    }

    fun setTabSelected(position: Int) {
        this.position = position
        val tabCount = tabsContainer.childCount
        for (i in 0 until tabCount) {
            val child = tabsContainer.getChildAt(i)
            val isSelected = i == position
            if (indicatorType == TAB_INDICATOR_BACKGROUND) child.isSelected = isSelected
            if (isSelected) {
                animateToTab(position)
            }
        }
    }

    fun animateToTab(position: Int) {
        val tabView = tabsContainer.getChildAt(position)
        if (runnable != null) {
            removeCallbacks(runnable)
        }
        runnable = Runnable {
            val scrollPos = tabView.left - (width - tabView.width) / 2
            smoothScrollTo(scrollPos, 0)
            runnable = null
        }
        post(runnable)
    }

    private fun scrollToChild(position: Int, offset: Int) {
        val tabView = tabsContainer.getChildAt(position)
        val newScrollX = tabView.left - (width - tabView.width) / 2 + offset
        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX
            scrollTo(newScrollX, 0)
        }
    }

    fun setTextSize(textSize: Int): PagerTabsIndicator {
        this.textSize = textSize
        return this
    }

    fun setIndicatorType(indicatorType: Int): PagerTabsIndicator {
        this.indicatorType = indicatorType
        return this
    }

    fun setIndicatorHeight(indicatorHeight: Int): PagerTabsIndicator {
        this.indicatorHeight = indicatorHeight
        return this
    }

    fun setIndicatorBgHeight(indicatorBgHeight: Int): PagerTabsIndicator {
        this.indicatorBgHeight = indicatorBgHeight
        return this
    }

    fun setIndicatorMargin(indicatorMargin: Int): PagerTabsIndicator {
        this.indicatorMargin = indicatorMargin
        return this
    }

    fun setIndicatorColor(indicatorColor: Int): PagerTabsIndicator {
        this.indicatorColor = indicatorColor
        return this
    }

    fun setIndicatorBgColor(indicatorBgColor: Int): PagerTabsIndicator {
        this.indicatorBgColor = indicatorBgColor
        return this
    }

    fun setShowDivider(showDivider: Boolean): PagerTabsIndicator {
        isShowDivider = showDivider
        return this
    }

    fun setDividerWidth(dividerWidth: Int): PagerTabsIndicator {
        this.dividerWidth = dividerWidth
        return this
    }

    fun setDividerMargin(dividerMargin: Int): PagerTabsIndicator {
        this.dividerMargin = dividerMargin
        return this
    }

    fun setTabElevation(tabElevation: Int): PagerTabsIndicator {
        this.tabElevation = tabElevation
        return this
    }

    fun setDividerColor(dividerColor: Int): PagerTabsIndicator {
        this.dividerColor = dividerColor
        return this
    }

    fun setTextColor(textColor: Int): PagerTabsIndicator {
        this.textColor = textColor
        return this
    }

    fun setTabPadding(tabPadding: Int): PagerTabsIndicator {
        this.tabPadding = tabPadding
        return this
    }

    fun setLockExpanded(lockExpanded: Boolean): PagerTabsIndicator {
        isLockExpanded = lockExpanded
        return this
    }

    fun setShowBarIndicator(showBarIndicator: Boolean): PagerTabsIndicator {
        this.showBarIndicator = showBarIndicator
        return this
    }

    fun setDisableTabAnimation(disableTabAnimation: Boolean): PagerTabsIndicator {
        isDisableTabAnimation = disableTabAnimation
        return this
    }

    fun setDividerResource(dividerResource: Int): PagerTabsIndicator {
        if (dividerResource == -1) {
            dividerDrawable = null
        }
        this.dividerResource = dividerResource
        return this
    }

    fun setIndicatorResource(indicatorResource: Int): PagerTabsIndicator {
        this.indicatorResource = indicatorResource
        if (indicatorResource == -1) indicatorDrawable = null
        return this
    }

    fun setHighlightText(highlightText: Boolean): PagerTabsIndicator {
        isHighlightText = highlightText
        return this
    }

    fun setHighlightTextColor(highlightTextColor: Int): PagerTabsIndicator {
        this.highlightTextColor = highlightTextColor
        return this
    }

    fun refresh() {
        Log.d(TAG, "refresh")
        tabWidth = LayoutParams.WRAP_CONTENT
        invalidateViews()
    }

    fun setHeight(height: Int): PagerTabsIndicator {
        layoutParams.height = height
        requestLayout()
        return this
    }

    fun getTabAt(position: Int): View? {
        return if (position < tabsContainer.childCount) {
            tabsContainer.getChildAt(position)
        } else null
    }

    companion object {
        //CONSTANTS
        private val TAG = PagerTabsIndicator::class.java.simpleName
        const val TAB_INDICATOR_TOP = 0
        const val TAB_INDICATOR_BOTTOM = 1
        const val TAB_INDICATOR_BACKGROUND = 2
        const val SCALE_FIT_XY = 0
        const val SCALE_CENTER_INSIDE = 1
    }
}