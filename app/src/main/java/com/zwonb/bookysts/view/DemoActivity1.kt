package com.zwonb.bookysts.view

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.zwonb.bookysts.R
import kotlinx.android.synthetic.main.activity_demo1.*

/**
 * 外部拦截滑动冲突
 */

class DemoActivity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo1)

        initView()
    }

    private fun initView() {
        val inflater = layoutInflater
        val screenWidth = MyUtils.getScreenMetrics(this).widthPixels
        val screenHeight = MyUtils.getScreenMetrics(this).heightPixels
        for (i in 0..2) {
            val layout = inflater.inflate(R.layout.content_layout, container, false) as ViewGroup
            layout.layoutParams.width = screenWidth
            val textView = layout.findViewById<View>(R.id.title) as TextView
            textView.text = "page " + (i + 1)
            layout.setBackgroundColor(Color.rgb(255 / (i + 1), 255 / (i + 1), 0))
            createList(layout)
            container.addView(layout)
        }
    }

    private fun createList(layout: ViewGroup) {
        val listView = layout.findViewById<View>(R.id.list) as ListView
        val datas = ArrayList<String>()
        for (i in 0..49) {
            datas.add("name $i")
        }

        val adapter = ArrayAdapter(this, R.layout.content_list_item, R.id.name, datas)
        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, _, _ ->
            Toast.makeText(
                this@DemoActivity1, "click item",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
