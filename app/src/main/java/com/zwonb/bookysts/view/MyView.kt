package com.zwonb.bookysts.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * 事件分发机制
 *
 * @author zwonb
 * @date 2019/4/20
 */
class MyView : View {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.e("zwonb", "MyView==dispatchTouchEvent: ${event?.action}")
        return super.dispatchTouchEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e("zwonb", "MyView===onTouchEvent: ${event?.action}")
        return super.onTouchEvent(event)
    }
}