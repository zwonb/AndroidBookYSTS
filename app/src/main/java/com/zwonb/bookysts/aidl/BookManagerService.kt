package com.zwonb.bookysts.aidl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import android.util.Log
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

/**
 * aidl 服务端
 *
 * @author zwonb
 * @date 2019/5/13
 */
class BookManagerService : Service() {

    private val mBookList = CopyOnWriteArrayList<Book>()

    private var isServiceDestroyed = AtomicBoolean(false)
//    private val mListener = CopyOnWriteArrayList<IOnNewBookArrivedListener>()
    private val mListenerList = RemoteCallbackList<IOnNewBookArrivedListener>()

    override fun onCreate() {
        super.onCreate()
        mBookList.add(Book(1, "Android"))
        mBookList.add(Book(2, "Flutter"))

        thread { ServiceWorker().run() }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return getBinder()
    }

    private fun getBinder(): IBinder {
        return object : IBookManager.Stub() {
            override fun addBook(book: Book) {
                bookList.add(book)
            }

            override fun getBookList(): MutableList<Book> {
                return mBookList
            }

            override fun registerListener(listener: IOnNewBookArrivedListener?) {
//                if (!mListener.contains(listener)) {
//                    mListener.add(listener)
//                } else {
//                    Log.e("zwonb", "registerListener: 已经存在了")
//                }
                mListenerList.register(listener)
            }

            override fun unRegisterListener(listener: IOnNewBookArrivedListener?) {
//                if (mListener.contains(listener)) {
//                    mListener.remove(listener)
//                } else {
//                    Log.e("zwonb", "unRegisterListener: 不存在，无法取消")
//                }
//                Log.e("zwonb", "listener size: ${mListener.size}")
                mListenerList.unregister(listener)
            }
        }
    }

    private inner class ServiceWorker {
        fun run() {
            while (!isServiceDestroyed.get()) {
                try {
                    Thread.sleep(5000)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val bookId = mBookList.size + 1
                val book = Book(bookId, "来新书啦！$bookId")
                onNewBookArrived(book)
            }
        }

        private fun onNewBookArrived(book: Book) {
            mBookList.add(book)
//            Log.e("zwonb", "onNewBookArrived: ${mListener.size}")
//            for (listener in mListener) {
//                listener.onNewBookArrived(book)
//            }
            val i = mListenerList.beginBroadcast()
            for (index in 0 until i) {
                val listener = mListenerList.getBroadcastItem(index)
                try {
                    listener?.onNewBookArrived(book)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            mListenerList.finishBroadcast()
        }

    }

    override fun onDestroy() {
        isServiceDestroyed.set(true)
        super.onDestroy()
    }
}