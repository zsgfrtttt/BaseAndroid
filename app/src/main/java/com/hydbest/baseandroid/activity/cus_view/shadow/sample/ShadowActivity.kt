package com.hydbest.baseandroid.activity.cus_view.shadow.sample;

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.hydbest.baseandroid.R

class ShadowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shadow)
        findViewById<Button>(R.id.staticImageButton).setOnClickListener {
            startActivity(Intent(this@ShadowActivity, StaticImageActivity::class.java))
        }
        findViewById<Button>(R.id.recyclerViewImageButton).setOnClickListener {
            startActivity(Intent(this@ShadowActivity, RecyclerImagesActivity::class.java))
        }
    }
}
