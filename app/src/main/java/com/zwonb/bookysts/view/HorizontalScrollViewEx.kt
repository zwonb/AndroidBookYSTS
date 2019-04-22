package com.zwonb.bookysts.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.VelocityTracker
import android.widget.Scroller
import android.text.method.TextKeyListener.clear
import android.support.v4.view.VelocityTrackerCompat.getXVelocity
import android.view.View


/**
 * 滑动冲突解决方案
 *
 * @author zwonb
 * @date 2019/4/22
 */
class HorizontalScrollViewEx : ViewGroup {

    private var mChildrenSize: Int = 0
    private var mChildWidth: Int = 0
    private var mChildIndex: Int = 0

    // 分别记录上次滑动的坐标
    private var mLastX = 0
    private var mLastY = 0
    // 分别记录上次滑动的坐标(onInterceptTouchEvent)
    private var mLastXIntercept = 0
    private var mLastYIntercept = 0

    private val mScroller: Scroller by lazy { Scroller(context) }
    private val mVelocityTracker: VelocityTracker by lazy { VelocityTracker.obtain() }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercepted = false
        val x = ev.x
        val y = ev.y
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                intercepted = false
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                    intercepted = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastXIntercept
                val deltaY = y - mLastYIntercept
                intercepted = Math.abs(deltaX) > Math.abs(deltaY)
            }
            MotionEvent.ACTION_UP -> {
                intercepted = false
            }
        }

        mLastX = x.toInt()
        mLastY = y.toInt()
        mLastXIntercept = x.toInt()
        mLastYIntercept = y.toInt()
        return intercepted
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mVelocityTracker.addMovement(event)
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastX
                val deltaY = y - mLastY
                scrollBy(-deltaX, 0)
            }
            MotionEvent.ACTION_UP -> {
                val scrollX = scrollX
                val scrollToChildIndex = scrollX / mChildWidth
                mVelocityTracker.computeCurrentVelocity(1000)
                val xVelocity = mVelocityTracker.xVelocity
                if (Math.abs(xVelocity) >= 50) {
                    mChildIndex = if (xVelocity > 0) mChildIndex - 1 else mChildIndex + 1
                } else {
                    mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth
                }
                mChildIndex = Math.max(0, Math.min(mChildIndex, mChildrenSize - 1))
                val dx = mChildIndex * mChildWidth - scrollX
                smoothScrollBy(dx, 0)
                mVelocityTracker.clear()
            }
            else -> {
            }
        }

        mLastX = x
        mLastY = y
        return true
    }

    private fun smoothScrollBy(dx: Int, dy: Int) {
        mScroller.startScroll(scrollX, 0, dx, 0, 500)
        invalidate()
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            postInvalidate()
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var measuredWidth = 0
        var measuredHeight = 0
        val childCount = childCount
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val widthSpaceSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpaceSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        when {
            childCount == 0 -> setMeasuredDimension(0, 0)
            heightSpecMode == MeasureSpec.AT_MOST -> {
                val childView = getChildAt(0)
                measuredHeight = childView.measuredHeight
                setMeasuredDimension(widthSpaceSize, childView.measuredHeight)
            }
            widthSpecMode == MeasureSpec.AT_MOST -> {
                val childView = getChildAt(0)
                measuredWidth = childView.measuredWidth * childCount
                setMeasuredDimension(measuredWidth, heightSpaceSize)
            }
            else -> {
                val childView = getChildAt(0)
                measuredWidth = childView.measuredWidth * childCount
                measuredHeight = childView.measuredHeight
                setMeasuredDimension(measuredWidth, measuredHeight)
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childLeft = 0
        val childCount = childCount
        mChildrenSize = childCount

        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView.visibility != View.GONE) {
                val childWidth = childView.measuredWidth
                mChildWidth = childWidth
                childView.layout(
                    childLeft, 0, childLeft + childWidth,
                    childView.measuredHeight
                )
                childLeft += childWidth
            }
        }
    }

    override fun onDetachedFromWindow() {
        mVelocityTracker.recycle()
        super.onDetachedFromWindow()
    }

}