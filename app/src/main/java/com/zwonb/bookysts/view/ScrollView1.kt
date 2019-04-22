package com.zwonb.bookysts.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 滑动冲突内部拦截
 *
 * @author zwonb
 * @date 2019/4/22
 */
class ScrollView1 : View {

    var lastX = 0
    var lastY = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val offsetX = x - lastX
                val offsetY = y - lastY
                if (offsetX > offsetY) {
                    // 父容器需要此类点击事件
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            MotionEvent.ACTION_UP -> {

            }
        }
        lastX = x.toInt()
        lastY = y.toInt()
        return super.dispatchTouchEvent(event)
    }

//    /**
//     * 父 ViewGroup 需要如下修改
//     */
//    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//        return when (ev.action) {
//            MotionEvent.ACTION_DOWN -> {
//                false
//            }
//            else -> {
//                true
//            }
//        }
//    }

}