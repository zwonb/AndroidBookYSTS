package com.zwonb.bookysts.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup

/**
 * 事件分发机制
 *
 * @author zwonb
 * @date 2019/4/20
 */
class MyViewGroup : ViewGroup {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}

    /**
     * 用来进行事件分发。
     * 如果事件能够传递给当前的 View，此方法一定被调用
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.e("zwonb", "MyViewGroup==dispatchTouchEvent: ${ev.action}")
        val dispatchTouchEvent = super.dispatchTouchEvent(ev)
        Log.e("zwonb", "dispatchTouchEvent: $dispatchTouchEvent")
        return dispatchTouchEvent
    }

    /**
     * 用来判断是否拦截某个事件。
     * 如果当前ViewGroup拦截了某个事件，那么在同一事件序列中，此方法不会再次调用
     */
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.e("zwonb", "MyViewGroup==onInterceptTouchEvent: ${ev?.action}")
        return super.onInterceptTouchEvent(ev)
    }

    /**
     * 用来表示是否消耗当前事件。
     * 如果不消耗，在同一事件系列中，当前View无法再次接收到事件
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e("zwonb", "MyViewGroup==onTouchEvent: ${event?.action}")
        return true
    }
}
