package com.hydbest.baseandroid.activity.cus_view.shadow.sample;

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
