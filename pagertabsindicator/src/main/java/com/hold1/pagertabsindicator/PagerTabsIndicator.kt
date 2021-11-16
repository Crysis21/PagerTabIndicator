package com.hold1.pagertabsindicator

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.annotation.RestrictTo
import androidx.core.graphics.toRectF
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.ViewPager
import com.hold1.pagertabsindicator.adapters.TabsAdapter
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Created by Cristian Holdunu on 08/11/2017.
 */

class PagerTabsIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : HorizontalScrollView(context, attrs) {
    private var tabsContainer: LinearLayout = LinearLayout(context)

    // Callbacks
    interface OnItemSelectedListener {
        fun onItemSelected(position: Int)
    }

    interface OnItemReselectedListener {
        fun onItemReselected(position: Int)
    }

    var onItemSelectedListener: OnItemSelectedListener? = null
    var onItemReselectedListener: OnItemReselectedListener? = null


    private var adapter: TabsAdapter? = null


    // Indicator Properties
    var showBarIndicator = true
        set(value) {
            field = value
            invalidate()
        }
    var indicatorType = TAB_INDICATOR_BOTTOM
        set(value) {
            field = value
            requestLayout()
        }
    var indicatorHeight = resources.getDimensionPixelSize(R.dimen.tab_default_indicator_height)
        set(value) {
            field = value
            invalidate()
        }
    var indicatorBackgroundHeight =
        resources.getDimensionPixelSize(R.dimen.tab_default_indicator_bg_height)
        set(value) {
            field = value
            invalidate()
        }
    var indicatorWidth: Int = -1
        set(value) {
            field = value
            invalidate()
        }
    var indicatorWidthPercent: Float = 0.0F
        set(value) {
            field = value
            invalidate()
        }
    var indicatorVerticalOffset = 0
        set(value) {
            field = value
            invalidate()
        }
    var indicatorCornerRadius: Float = 0.0f
        set(value) {
            field = value
            invalidate()
        }
    var indicatorColor = resources.getColor(R.color.tab_indicator_color)
        set(value) {
            field = value
            tintPaint.color = value
            invalidate()
        }
    var indicatorBgColor = resources.getColor(R.color.tab_indicator_bg_color)
        set(value) {
            field = value
            backgroundPaint.color = value
            invalidate()
        }
    var indicatorDrawable: Drawable? = null
        set(value) {
            field = value
            invalidate()
        }
    var indicatorResource = -1
        @SuppressLint("UseCompatLoadingForDrawables")
        set(value) {
            field = value
            indicatorDrawable = if (value == -1) {
                null
            } else {
                resources.getDrawable(value)
            }
        }
    var indicatorScaleType = SCALE_CENTER_INSIDE
        set(value) {
            field = value
            invalidate()
        }

    // Painting
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

    //Divider resources
    var isShowDivider = true
        set(value) {
            field = value
            invalidate()
        }
    var dividerWidth = resources.getDimensionPixelSize(R.dimen.tab_default_divider_width)
        set(value) {
            field = value
            invalidate()
        }
    var dividerMargin = resources.getDimensionPixelSize(R.dimen.tab_default_divider_margin)
        set(value) {
            field = value
            invalidate()
        }
    var tabElevation = 0
        set(value) {
            field = value
            ViewCompat.setElevation(this, value.toFloat())
        }
    var dividerColor = resources.getColor(R.color.tab_default_divider_color)
        set(value) {
            field = value
            dividerPaint.color = value
            invalidate()
        }
    var dividerResource = -1
        @SuppressLint("UseCompatLoadingForDrawables")
        set(value) {
            field = value
            dividerDrawable = if (value == -1) {
                null
            } else {
                resources.getDrawable(value)
            }
        }
    var dividerDrawable: Drawable? = null
        set(value) {
            field = value
            invalidate()
        }

    // Tab properties
    var isHighlightText = false
        set(value) {
            field = value
            invalidate()
        }
    var textSize = resources.getDimensionPixelSize(R.dimen.tab_default_text_size)
        set(value) {
            field = value
            invalidateViews()
        }
    var textColor = resources.getColor(R.color.tab_default_text_color)
        set(value) {
            field = value
            invalidateViews()
        }
    var highlightTextColor = resources.getColor(R.color.tab_indicator_color)
        set(value) {
            field = value
            invalidateViews()
        }
    var tabPadding = resources.getDimensionPixelSize(R.dimen.tab_default_padding)
        set(value) {
            field = value
            invalidate()
        }

    // Generic properties
    var isLockExpanded = false
        set(value) {
            field = value
            requestLayout()
        }
    var isDisableTabAnimation = false
        set(value) {
            field = value
            invalidate()
        }

    private var position = -1
    private var targetPosition = -1
    private var positionOffset = 0f
    private var lastScrollX = 0
    private var tabWidth = ViewGroup.LayoutParams.WRAP_CONTENT
    private var runnable: Runnable? = null
    private val indicatorBackgroundRect = RectF()
    private val indicatorRect = Rect()
    private val valueAnimator = ValueAnimator.ofInt().apply {
        this.interpolator = LinearInterpolator()
        this.duration = 200
    }

    init {
        isHorizontalScrollBarEnabled = false
        addView(tabsContainer, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerTabsIndicator)
        tabPadding =
            typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_padding, tabPadding)
        textSize =
            typedArray.getDimensionPixelSize(R.styleable.PagerTabsIndicator_tab_text_size, textSize)
        textColor = typedArray.getColor(R.styleable.PagerTabsIndicator_tab_text_color, textColor)
        highlightTextColor = typedArray.getColor(
            R.styleable.PagerTabsIndicator_tab_highlight_text_color,
            highlightTextColor
        )
        isHighlightText = typedArray.getBoolean(
            R.styleable.PagerTabsIndicator_tab_highlight_text_color,
            isHighlightText
        )
        isShowDivider =
            typedArray.getBoolean(R.styleable.PagerTabsIndicator_tab_show_divider, isShowDivider)
        isLockExpanded =
            typedArray.getBoolean(R.styleable.PagerTabsIndicator_tab_lock_expanded, isLockExpanded)
        indicatorType =
            typedArray.getInt(R.styleable.PagerTabsIndicator_tab_indicator, indicatorType)
        indicatorResource = typedArray.getResourceId(
            R.styleable.PagerTabsIndicator_tab_indicator_resource,
            indicatorResource
        )
        indicatorHeight = typedArray.getDimensionPixelSize(
            R.styleable.PagerTabsIndicator_tab_indicator_height,
            indicatorHeight
        )
        indicatorBackgroundHeight = typedArray.getDimensionPixelSize(
            R.styleable.PagerTabsIndicator_tab_indicator_background_height,
            indicatorBackgroundHeight
        )
        indicatorVerticalOffset = typedArray.getDimensionPixelSize(
            R.styleable.PagerTabsIndicator_tab_indicator_vertical_offset,
            indicatorVerticalOffset
        )
        indicatorColor =
            typedArray.getColor(R.styleable.PagerTabsIndicator_tab_indicator_color, indicatorColor)
        indicatorBgColor = typedArray.getColor(
            R.styleable.PagerTabsIndicator_tab_indicator_background_color,
            indicatorBgColor
        )
        dividerWidth = typedArray.getDimensionPixelSize(
            R.styleable.PagerTabsIndicator_tab_divider_width,
            dividerWidth
        )
        dividerMargin = typedArray.getDimensionPixelSize(
            R.styleable.PagerTabsIndicator_tab_divider_margin,
            dividerMargin
        )
        dividerColor =
            typedArray.getColor(R.styleable.PagerTabsIndicator_tab_divider_color, dividerColor)
        dividerResource = typedArray.getResourceId(
            R.styleable.PagerTabsIndicator_tab_divider_resource,
            dividerResource
        )
        showBarIndicator = typedArray.getBoolean(
            R.styleable.PagerTabsIndicator_tab_show_bar_indicator,
            showBarIndicator
        )
        tabElevation = typedArray.getDimensionPixelSize(
            R.styleable.PagerTabsIndicator_tab_elevation,
            tabElevation
        )
        indicatorScaleType = typedArray.getInt(
            R.styleable.PagerTabsIndicator_tab_indicator_scale_type,
            indicatorScaleType
        )
        isDisableTabAnimation = typedArray.getBoolean(
            R.styleable.PagerTabsIndicator_tab_disable_animation,
            isDisableTabAnimation
        )
        indicatorCornerRadius = typedArray.getDimensionPixelSize(
            R.styleable.PagerTabsIndicator_tab_indicator_corner_radius,
            0
        ).toFloat()
        indicatorWidth = typedArray.getDimensionPixelSize(
            R.styleable.PagerTabsIndicator_tab_indicator_width,
            indicatorWidth
        )
        indicatorWidthPercent = typedArray.getFloat(
            R.styleable.PagerTabsIndicator_tab_indicator_width_percent,
            indicatorWidthPercent
        )
        typedArray.recycle()

        valueAnimator.addUpdateListener {
            setIndicatorBounds(it.animatedValue as Int)
            invalidate()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return !isLockExpanded && super.onInterceptTouchEvent(ev)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        getTabAt(position)?.let { setIndicatorBounds(it.getCenterX()) }
        if (!changed) return
        val newWidth = r - l
        val childCount = tabsContainer.childCount
        if (isLockExpanded && childCount > 0) {
            val newTabWidth = newWidth / childCount
            if (newTabWidth == 0) return
            tabWidth = newTabWidth
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
                lp.topMargin = indicatorBackgroundHeight
                lp.bottomMargin = 0
            }
            TAB_INDICATOR_BOTTOM -> {
                lp.bottomMargin = indicatorBackgroundHeight
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
        tabsContainer.addView(
            view,
            LinearLayout.LayoutParams(tabWidth, ViewGroup.LayoutParams.MATCH_PARENT)
        )
        view?.setPadding(tabPadding, 0, tabPadding, 0)
        view?.setOnClickListener {
            Log.d(TAG, "tab click $position")
            if (this.position == position) {
                onItemReselectedListener?.onItemReselected(position)
            } else {
                onItemSelectedListener?.onItemSelected(position)
            }
            targetPosition = position
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawIndicatorBackground(canvas)
        drawIndicator(canvas)
        drawDivider(canvas)
    }

    private fun drawIndicatorBackground(canvas: Canvas) {
        indicatorBackgroundRect.left = 0f
        indicatorBackgroundRect.right = max(right, tabsContainer.width).toFloat()

        when (indicatorType) {
            TAB_INDICATOR_TOP -> {
                indicatorBackgroundRect.top = 0f
                indicatorBackgroundRect.bottom = indicatorBackgroundHeight.toFloat()
            }
            TAB_INDICATOR_BOTTOM -> {
                indicatorBackgroundRect.top = (height - indicatorBackgroundHeight).toFloat()
                indicatorBackgroundRect.bottom = height.toFloat()
            }
            else -> {
                indicatorBackgroundRect.top = (height - indicatorBackgroundHeight).toFloat()
                indicatorBackgroundRect.bottom = height.toFloat()
            }
        }
        canvas.drawRoundRect(
            indicatorBackgroundRect,
            indicatorCornerRadius,
            indicatorCornerRadius,
            backgroundPaint
        )
    }

    private fun drawIndicator(canvas: Canvas) {
        if (!showBarIndicator) return

        //TODO: move to layout part
        when (indicatorType) {
            TAB_INDICATOR_TOP -> {
                indicatorRect.top = indicatorVerticalOffset
                indicatorRect.bottom = indicatorHeight + indicatorVerticalOffset
            }
            TAB_INDICATOR_BOTTOM -> {
                indicatorRect.top = height - indicatorHeight - indicatorVerticalOffset
                indicatorRect.bottom = height - indicatorVerticalOffset
            }
            else -> {
                indicatorRect.top = height - indicatorHeight - indicatorVerticalOffset
                indicatorRect.bottom = height - indicatorVerticalOffset
            }
        }

        indicatorDrawable?.let { drawable ->
            if (indicatorScaleType == SCALE_CENTER_INSIDE) {
                //Adjust the indicator bounds aspect ratio to keep the indicator resource intact
                val ratio =
                    (drawable.intrinsicHeight / drawable.intrinsicWidth).toFloat()
                val scaledWidth = indicatorHeight * ratio
                indicatorRect.left = (indicatorRect.centerX() - scaledWidth / 2).toInt()
                indicatorRect.right = (indicatorRect.left + scaledWidth).toInt()
            }
            drawable.bounds = indicatorRect
            drawable.draw(canvas)
        } ?: run {
            canvas.drawRoundRect(
                indicatorRect.toRectF(),
                indicatorCornerRadius,
                indicatorCornerRadius,
                tintPaint
            )
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
                endY = height - indicatorBackgroundHeight - dividerMargin
            } else if (indicatorType == TAB_INDICATOR_TOP) {
                startY = indicatorBackgroundHeight + dividerMargin
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

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
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

        if (isDisableTabAnimation) {
            tabsContainer.getChildAt((position + positionOffset).roundToInt())?.let { nextTab ->
                setIndicatorBounds(nextTab.getCenterX())

            }
        } else {
            tabsContainer.getChildAt(position)?.let { currentTab ->
                setIndicatorBounds(currentTab.getCenterX())
            }
            adapter?.let {
                if (positionOffset > 0f && position < it.getCount() - 1 && !isDisableTabAnimation) {
                    val nextTab = tabsContainer.getChildAt(position + 1)
                    setIndicatorBounds((positionOffset * nextTab.getCenterX() + (1f - positionOffset) * indicatorRect.centerX()).toInt())
                }
            }
        }
        invalidate()
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun onPageSelected(position: Int) {
        Log.d(TAG, "onPageSelected=$position")
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun onPageScrollStateChanged(state: Int) {
        Log.d(TAG, "onPageScrollStateChanged $state")
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (targetPosition != -1) setTabSelected(targetPosition, false)
            targetPosition = -1
        }
    }

    fun setTabSelected(position: Int, animated: Boolean = true) {
        Log.d(TAG, "setTabSelected $position")
        var startX = -1
        var endX = -1
        getTabAt(this.position)?.let {
            startX = it.getCenterX()
        }
        getTabAt(position)?.let {
            endX = it.getCenterX()
        }
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
        if (animated && startX != -1 && endX != -1) {
            valueAnimator.cancel()
            valueAnimator.setIntValues(indicatorRect.centerX(), endX)
            valueAnimator.start()
        } else {
            setIndicatorBounds(endX)
        }
    }

    private fun setIndicatorBounds(withCenter: Int) {
        val drawWidth = when {
            indicatorWidthPercent > 0 -> (tabWidth * indicatorWidthPercent).toInt()
            indicatorWidth > -1 -> indicatorWidth
            else -> tabWidth
        }

        indicatorRect.left = (withCenter - drawWidth / 2)
        indicatorRect.right = indicatorRect.left + drawWidth
    }

    private fun animateToTab(position: Int) {
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

fun View.getCenterX(): Int {
    return right - measuredWidth / 2
}