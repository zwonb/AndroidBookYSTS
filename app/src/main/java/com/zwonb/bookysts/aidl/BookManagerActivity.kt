package com.zwonb.bookysts.aidl

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Log

/**
 * aidl 客户端
 *
 * @author zwonb
 * @date 2019/5/13
 */
class BookManagerActivity : AppCompatActivity() {

    private val serviceConnection by lazy { getConnection() }

    private var bookManager: IBookManager? = null
    private val handle = object : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                1 -> {
                    Log.e("zwonb", "收到消息: ${msg.obj}")
                }
                else -> {
                    super.handleMessage(msg)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, BookManagerService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun getConnection(): ServiceConnection {
        return object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val bookManager = IBookManager.Stub.asInterface(service)
                try {
                    this@BookManagerActivity.bookManager = bookManager

                    val list = bookManager.bookList
                    Log.e("zwonb", "list type: ${list.javaClass.canonicalName}")
                    Log.e("zwonb", "list: $list")

                    val book = Book(3, "Android开发艺术探索")
                    bookManager.addBook(book)
                    val newList = bookManager.bookList
                    Log.e("zwonb", "添加后 list: $newList")

                    bookManager.registerListener(listener)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onServiceDisconnected(name: ComponentName?) {
                bookManager = null
                Log.e("zwonb", "onServiceDisconnected: binder died")
            }

        }
    }

    private val listener = object : IOnNewBookArrivedListener.Stub() {
        override fun onNewBookArrived(book: Book?) {
            handle.obtainMessage(1, book).sendToTarget()
        }
    }

    override fun onDestroy() {
        if (bookManager != null && bookManager!!.asBinder().isBinderAlive) {
            try {
                bookManager!!.unRegisterListener(listener)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        unbindService(serviceConnection)
        handle.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}