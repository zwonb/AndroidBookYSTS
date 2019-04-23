package com.zwonb.bookysts.handle

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlin.concurrent.thread

/**
 * ThreadLocal
 *
 * @author zwonb
 * @date 2019/4/22
 */
class ThreadLocalTest : AppCompatActivity() {

    private var threadLocal = ThreadLocal<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        threadLocal.set(true)
        // 输出true
        Log.e("zwonb", "ActivityThread: ${threadLocal.get()}")

        Thread({
            threadLocal.set(false)
            // 输出false
            Log.e("zwonb", "thread1: ${threadLocal.get()}")
        }, "thread1").start()

        Thread({
            // 输出null
            Log.e("zwonb", "thread2: ${threadLocal.get()}")
        }, "thread2").start()
    }
}