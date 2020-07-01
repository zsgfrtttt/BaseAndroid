package com.hydbest.baseandroid.activity.cus_view.shadow.sample;

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hydbest.baseandroid.R
import io.github.armcha.coloredshadow.ShadowImageView


class StaticImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_static_image)
        val image = findViewById<ShadowImageView>(R.id.shadowImage).apply {
            radiusOffset = 0.4f
            setImageResource(R.drawable.android)
        }

        findViewById<Button>(R.id.red).setOnClickListener {
            image.shadowColor = ContextCompat.getColor(this@StaticImageActivity, R.color.red)
            image.setImageResource(R.drawable.android)
        }
        findViewById<Button>(R.id.blue).setOnClickListener {
            image.shadowColor = ContextCompat.getColor(this@StaticImageActivity, R.color.blue)
            image.setImageResource(R.drawable.android)
        }
        findViewById<Button>(R.id.green).setOnClickListener {
            image.shadowColor = ContextCompat.getColor(this@StaticImageActivity, R.color.green)
            image.setImageResource(R.drawable.android)
        }
        findViewById<Button>(R.id.gray).setOnClickListener {
            image.shadowColor = ContextCompat.getColor(this@StaticImageActivity, R.color.gray)
            image.setImageResource(R.drawable.android)
        }
    }
}
