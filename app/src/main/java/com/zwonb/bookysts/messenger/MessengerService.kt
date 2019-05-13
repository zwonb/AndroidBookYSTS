package com.zwonb.bookysts.messenger

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log

/**
 * 说明
 *
 * @author zwonb
 * @date 2019/5/10
 */
class MessengerService : Service() {

    private val messenger: Messenger by lazy { Messenger(MessengerHandle()) }

    override fun onBind(intent: Intent?): IBinder? {
        return messenger.binder
    }

}

class MessengerHandle : Handler() {

    override fun handleMessage(msg: Message?) {
        when (msg?.what) {
            1 -> {
                Log.e("zwonb", "收到客户端信息: ${msg.data["msg"]}")
                val client = msg.replyTo
                val replyMsg = Message.obtain(null, 2)
                val bundle = Bundle()
                bundle.putString("msg", "回复一个String")
                replyMsg.data = bundle
                try {
                    client.send(replyMsg)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            else -> {
                super.handleMessage(msg)
            }
        }
    }
}