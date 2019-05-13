package com.zwonb.bookysts.messenger

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log

/**
 * 客户端
 *
 * @author zwonb
 * @date 2019/5/10
 */
class MessengerActivity : AppCompatActivity() {

    private var serviceMessenger: Messenger? = null
    private val getReplyMessenger: Messenger by lazy { Messenger(MessengerHandle()) }
    private val serviceConnection: ServiceConnection by lazy { getConnection() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MessengerService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun getConnection(): ServiceConnection {
        return object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                serviceMessenger = Messenger(service)
                val msg = Message.obtain(null, 1)
                val bundle = Bundle()
                bundle.putString("msg", "传个String给你")
                msg.data = bundle

                msg.replyTo = getReplyMessenger
                try {
                    serviceMessenger?.send(msg)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }

    class MessengerHandle : Handler(){
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                2 -> {
                    Log.e("zwonb", "收到服务端信息: ${msg.data["msg"]}")
                }
                else -> {
                    super.handleMessage(msg)
                }
            }
        }

    }
}