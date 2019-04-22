package com.zwonb.bookysts.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup

/**
 * 滑动冲突外部拦截
 *
 * @author zwonb
 * @date 2019/4/20
 */
class ScrollViewGroup : ViewGroup {

    private var lastX = 0
    private var lastY = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}

    /**
     * 父 ViewGroup 拦截，解决滑动冲突
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var interceptor = false
        val x = ev.x
        val y = ev.y
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> interceptor = false
            MotionEvent.ACTION_MOVE -> {
                if (x - y > 0) {
                    // 父容器需要当前点击事件
                    interceptor = true
                } else {
                    interceptor = false
                }
            }
            MotionEvent.ACTION_UP -> interceptor = false
        }
        lastX = x.toInt()
        lastY = y.toInt()
        return interceptor
    }

}