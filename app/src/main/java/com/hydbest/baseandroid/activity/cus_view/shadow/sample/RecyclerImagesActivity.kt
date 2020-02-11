package com.hydbest.baseandroid.activity.cus_view.shadow.sample;

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.hydbest.baseandroid.R

class RecyclerImagesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_images)
        findViewById<RecyclerView>(R.id.recycler).apply {
            adapter = ImageAdapter()
            layoutManager = LinearLayoutManager(this@RecyclerImagesActivity)
        }
    }
}
