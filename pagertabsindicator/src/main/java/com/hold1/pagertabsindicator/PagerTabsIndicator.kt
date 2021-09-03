package com.hold1.pagertabsindicator

import android.content.Context
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnAdapterChangeListener
import com.hold1.pagertabsindicator.TabViewProvider
import android.database.DataSetObserver
import androidx.viewpager.widget.PagerAdapter
import com.hold1.pagertabsindicator.PagerTabsIndicator
import android.view.ViewGroup
import com.hold1.pagertabsindicator.R
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup.MarginLayoutParams
import com.hold1.pagertabsindicator.TabViewProvider.CustomView
import android.view.Gravity
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide

/**
 * Created by Cristian Holdunu on 08/11/2017.
 */
class PagerTabsIndicator : HorizontalScrollView, OnPageChangeListener {
    private var tabsContainer: LinearLayout? = null
    private var viewPager: ViewPager? = null
    private var adapterChangeListener: OnAdapterChangeListener? = null
    private val tabProvider: TabViewProvider? = null
    private var adapterObserver: DataSetObserver? = null
    private var adapter: PagerAdapter? = null

    //Getters and setters that should behave like a builder pattern
    var textSize = 0
        private set

    /**
     * Tab Indicator
     */
    var indicatorType = TAB_INDICATOR_BOTTOM
        private set
    var indicatorHeight = 20
        private set
    var indicatorBgHeight = 20
        private set
    var indicatorMargin = 0
        private set
    var indicatorColor = 0
        private set
    var indicatorBgColor = 0
        private set
    private var indicatorDrawable: Drawable? = null

    //Use this if you want a custom resource to be drawn as tab indicator
    private var bgPaing: Paint? = null
    private var indicatorResource = -1
    private var indicatorScaleType = SCALE_CENTER_INSIDE
    private var tintPaint: Paint? = null
    private var dividerPaint: Paint? = null

    /**
     * Tab Divider
     */
    var isShowDivider = true
        private set
    var dividerWidth = 2
        private set
    var dividerMargin = 10
        private set
    var tabElevation = 6
        private set
    var dividerColor = Color.BLACK
        private set

    // Use this if you want a custom resource drawn in tab divider
    private var dividerResource = -1
    private var dividerDrawable: Drawable? = null
    var isHighlightText = false
        private set
    var textColor = 0
        private set
    var highlightTextColor = 0
        private set
    var tabPadding = 0
        private set
    var isLockExpanded = false
        private set
    var isShowBarIndicator = true
        private set
    var isDisableTabAnimation = false
        private set

    //ViePager data
    private var position = 0
    private val oldPosition = 0
    private var targetPosition = -1
    private var positionOffset = 0f
    private var lastScrollX = 0
    private var tabWidth = ViewGroup.LayoutParams.WRAP_CONTENT
    private var runnable: Runnable? = null
    private val bgRect = RectF()
    private val indicatorRect = Rect()

    constructor(context: Context?) : super(context, null) {}

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        isHorizontalScrollBarEnabled = false
        tabsContainer = LinearLayout(getContext())
        addView(tabsContainer, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        //Load default dimens
        textSize = getContext().resources.getDimensionPixelSize(R.dimen.tab_default_text_size)
        indicatorHeight =
            getContext().resources.getDimensionPixelSize(R.dimen.tab_default_indicator_height)
        indicatorBgHeight =
            getContext().resources.getDimensionPixelSize(R.dimen.tab_default_indicator_bg_height)
        dividerWidth =
            getContext().resources.getDimensionPixelSize(R.dimen.tab_default_divider_width)
        dividerMargin =
            getContext().resources.getDimensionPixelSize(R.dimen.tab_default_divider_margin)

        //Load default colors
        textColor = resources.getColor(R.color.tab_default_text_color)
        indicatorColor = resources.getColor(R.color.tab_indicator_color)
        highlightTextColor = indicatorColor
        indicatorBgColor = resources.getColor(R.color.tab_indicator_bg_color)
        dividerColor = resources.getColor(R.color.tab_default_divider_color)
        tabPadding = resources.getDimensionPixelSize(R.dimen.tab_default_padding)
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
        indicatorBgHeight = typedArray.getDimensionPixelSize(
            R.styleable.PagerTabsIndicator_tab_indicator_bg_height,
            indicatorBgHeight
        )
        indicatorMargin = typedArray.getDimensionPixelSize(
            R.styleable.PagerTabsIndicator_tab_indicator_margin,
            indicatorMargin
        )
        indicatorColor =
            typedArray.getColor(R.styleable.PagerTabsIndicator_tab_indicator_color, indicatorColor)
        indicatorBgColor = typedArray.getColor(
            R.styleable.PagerTabsIndicator_tab_indicator_bg_color,
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
        isShowBarIndicator = typedArray.getBoolean(
            R.styleable.PagerTabsIndicator_tab_show_bar_indicator,
            isShowBarIndicator
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
        typedArray.recycle()
        prepareResources()
        adapterObserver = object : DataSetObserver() {
            override fun onChanged() {
                notifyDatasetChanged()
            }

            override fun onInvalidated() {
                super.onInvalidated()
            }
        }
    }

    private fun prepareResources() {
        bgPaing = Paint()
        bgPaing!!.color = indicatorBgColor
        bgPaing!!.style = Paint.Style.FILL
        bgPaing!!.isAntiAlias = true
        tintPaint = Paint()
        tintPaint!!.color = indicatorColor
        tintPaint!!.style = Paint.Style.FILL
        tintPaint!!.isAntiAlias = true
        dividerPaint = Paint()
        dividerPaint!!.color = dividerColor
        dividerPaint!!.style = Paint.Style.FILL
        dividerPaint!!.isAntiAlias = true
        if (indicatorResource != -1) {
            indicatorDrawable = resources.getDrawable(indicatorResource)
        }
        if (dividerResource != -1) {
            dividerDrawable = resources.getDrawable(dividerResource)
        }
        ViewCompat.setElevation(this, tabElevation.toFloat())
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return !isLockExpanded && super.onInterceptTouchEvent(ev)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        Log.d(TAG, "layout width=" + (r - l))
        if (!changed) return
        val newWidth = r - l
        val childCount = tabsContainer!!.childCount
        if (isLockExpanded && childCount > 0) {
            val newTabWidth = newWidth / childCount
            if (newTabWidth == 0) return
            tabWidth = newTabWidth
            Log.d(TAG, "newWidth=$newWidth tabWidth=$tabWidth")
            for (i in 0 until tabsContainer!!.childCount) {
                val view = tabsContainer!!.getChildAt(i)
                view.layoutParams.width = tabWidth
                view.requestLayout()
            }
            tabsContainer!!.layoutParams = tabsContainer!!.layoutParams
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
        if (isShowBarIndicator) when (indicatorType) {
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

    fun setViewPager(viewPager: ViewPager?) {
        if (viewPager == null) {
            tabsContainer!!.removeAllViews()
            if (this.viewPager != null) {
                this.viewPager!!.removeOnAdapterChangeListener(adapterChangeListener!!)
                this.viewPager!!.removeOnPageChangeListener(this)
            }
            this.viewPager = viewPager
            return
        }
        this.viewPager = viewPager
        this.viewPager!!.addOnPageChangeListener(this)
        adapterChangeListener = OnAdapterChangeListener { viewPager, oldAdapter, newAdapter ->
            listenToAdapterChanges(newAdapter)
        }
        adapterChangeListener?.let {
            viewPager.addOnAdapterChangeListener(it)
        }

        listenToAdapterChanges(viewPager.adapter)
    }

    fun setTabProvider(tabProvider: TabViewProvider?) {
        if (viewPager != null) {
            viewPager!!.removeOnPageChangeListener(this)
            viewPager!!.removeOnAdapterChangeListener(adapterChangeListener!!)
        }
    }

    fun notifyDatasetChanged() {
        if (viewPager == null || viewPager!!.adapter == null) return
        tabsContainer!!.removeAllViews()
        for (i in 0 until viewPager!!.adapter!!.count) {
            var innerView: View?
            if (viewPager!!.adapter is CustomView) {
                innerView = (viewPager!!.adapter as CustomView?)!!.getView(i)
            } else if (viewPager!!.adapter is TabViewProvider.ImageProvider) {
                val imageProvider = viewPager!!.adapter as TabViewProvider.ImageProvider?
                innerView = createImageView()
                if (imageProvider!!.getImageUri(i) != null) {
                    Glide.with(context).load(imageProvider.getImageUri(i))
                        .into(innerView as ImageView)
                } else if (imageProvider.getImageResourceId(i) != 0) {
                    (innerView as ImageView?)!!.setImageResource(imageProvider.getImageResourceId(i))
                }
                innerView = TabView(context, innerView)
            } else {
                innerView = createTextView(viewPager!!.adapter!!.getPageTitle(i).toString())
            }
            addTabView(innerView, i)
        }
    }

    private fun createTextView(text: String): View {
        val textView = TextView(context)
        textView.text = text
        textView.gravity = Gravity.CENTER
        textView.setSingleLine()
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        textView.setTextColor(textColor)
        return object : TabView(this@PagerTabsIndicator.context, textView) {
            override fun onOffset(offset: Float) {
                super.onOffset(offset)
                if (isHighlightText) {
                    (getChildAt(0) as TextView).setTextColor(
                        Util.mixTwoColors(
                            highlightTextColor,
                            textColor,
                            offset
                        )
                    )
                }
            }
        }
    }

    private fun createImageView(): View {
        val imageView = ImageView(context)
        imageView.setPadding(tabPadding, tabPadding, tabPadding, tabPadding)
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.adjustViewBounds = true
        return imageView
    }

    private fun addTabView(view: View?, position: Int) {
        tabsContainer!!.addView(
            view,
            LinearLayout.LayoutParams(tabWidth, ViewGroup.LayoutParams.MATCH_PARENT)
        )
        view!!.setPadding(tabPadding, 0, tabPadding, 0)
        view.setOnClickListener {
            Log.d(TAG, "tab click $position")
            viewPager!!.currentItem = position
            targetPosition = position
        }
    }

    private fun listenToAdapterChanges(pagerAdapter: PagerAdapter?) {
        if (adapter != null) {
            adapter!!.unregisterDataSetObserver(adapterObserver!!)
        }
        if (pagerAdapter == null) {
            Log.e(TAG, "listenToAdapterChanges - pager adapter is null. can't register")
            return
        }
        adapter = pagerAdapter
        adapter!!.registerDataSetObserver(adapterObserver!!)
        notifyDatasetChanged()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawIndicator(canvas)
        drawDivider(canvas)
    }

    private fun drawIndicator(canvas: Canvas) {
        if (!isShowBarIndicator) return
        bgRect.left = 0f
        bgRect.right = Math.max(right, tabsContainer!!.width).toFloat()
        var currentTab: View? = tabsContainer!!.getChildAt(position) ?: return
        if (isDisableTabAnimation) {
            currentTab = tabsContainer!!.getChildAt(Math.round(position + positionOffset))
        }
        indicatorRect.left = currentTab!!.left
        indicatorRect.right = currentTab.right
        if (positionOffset > 0f && position < viewPager!!.adapter!!.count - 1 && !isDisableTabAnimation) {
            val nextTab = tabsContainer!!.getChildAt(position + 1)
            indicatorRect.left =
                (positionOffset * nextTab.left + (1f - positionOffset) * indicatorRect.left).toInt()
            indicatorRect.right =
                (positionOffset * nextTab.right + (1f - positionOffset) * indicatorRect.right).toInt()
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
        canvas.drawRect(bgRect, bgPaing)
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
        for (i in 0 until tabsContainer!!.childCount - 1) {
            val tab = tabsContainer!!.getChildAt(i)
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
            if (dividerDrawable == null) canvas.drawRect(
                startX.toFloat(),
                startY.toFloat(),
                endX.toFloat(),
                endY.toFloat(),
                dividerPaint
            ) else {
                dividerDrawable!!.bounds = Rect(startX, startY, endX, endY)
                dividerDrawable!!.draw(canvas)
            }
        }
    }

    //Listen View Pager events
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        Log.d(
            TAG,
            "onPageScrolled:position=" + position + ";positionOffset:" + positionOffset + ";tabsContainer child count:" + tabsContainer!!.childCount
        )
        this.position = position
        this.positionOffset = positionOffset
        if (targetPosition == -1 && tabsContainer!!.childCount > position) {
            scrollToChild(
                position,
                (positionOffset * tabsContainer!!.getChildAt(position).width).toInt()
            )
        }
        val targetPosition = Math.round(position + positionOffset)
        val targetOffset = Math.abs(0.5 - positionOffset).toFloat() * 2
        for (i in 0 until tabsContainer!!.childCount) {
            val child = tabsContainer!!.getChildAt(i) as? TabView ?: continue
            if (i == targetPosition) {
                child.onOffset(targetOffset)
            } else {
                child.onOffset(0f)
            }
        }
        invalidate()
    }

    override fun onPageSelected(position: Int) {
        Log.d(TAG, "onPageSelected=$position")
    }

    override fun onPageScrollStateChanged(state: Int) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (targetPosition != -1) setTabSelected(targetPosition)
            targetPosition = -1
        }
    }

    fun setTabSelected(position: Int) {
        this.position = position
        val tabCount = tabsContainer!!.childCount
        for (i in 0 until tabCount) {
            val child = tabsContainer!!.getChildAt(i)
            val isSelected = i == position
            if (indicatorType == TAB_INDICATOR_BACKGROUND) child.isSelected = isSelected
            if (isSelected) {
                animateToTab(position)
            }
        }
    }

    fun animateToTab(position: Int) {
        val tabView = tabsContainer!!.getChildAt(position)
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
        val tabView = tabsContainer!!.getChildAt(position)
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
        isShowBarIndicator = showBarIndicator
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
        prepareResources()
        notifyDatasetChanged()
    }

    fun setHeight(height: Int): PagerTabsIndicator {
        layoutParams.height = height
        requestLayout()
        return this
    }

    fun getTabAt(position: Int): View? {
        return if (position < tabsContainer!!.childCount) {
            tabsContainer!!.getChildAt(position)
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