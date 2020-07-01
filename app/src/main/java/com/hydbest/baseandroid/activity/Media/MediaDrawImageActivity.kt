package com.hydbest.baseandroid.activity.Media

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.os.Bundle
import android.os.Environment
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.hydbest.baseandroid.R
import kotlinx.android.synthetic.main.activity_media_draw_image.*
import java.io.File

/**
 * Created by csz on 2018/9/13.
 */
class MediaDrawImageActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_draw_image)
    }

    fun drawByFatory(view: View?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        } else {
            iv.setVisibility(View.VISIBLE)
            surface!!.visibility = View.GONE
            val bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + File.separator + "img.jpg")
            iv.setImageBitmap(bitmap)
        }
    }

    fun drawBySurface(view: View?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        } else {
            iv.setVisibility(View.GONE)
            surface.setVisibility(View.VISIBLE)
            surface.getHolder().addCallback(object : SurfaceHolder.Callback2 {
                override fun surfaceRedrawNeeded(holder: SurfaceHolder) {}
                override fun surfaceCreated(holder: SurfaceHolder) {
                    if (holder == null) return
                    var bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + File.separator + "img.jpg")
                    val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
                    //paint.setStyle(Paint.Style.STROKE);
                    val canvas = holder.lockCanvas()
                    bitmap = Bitmap.createScaledBitmap(bitmap!!, surface.getWidth(), surface.getHeight(), false)
                    canvas.drawBitmap(bitmap, 0f, 0f, paint)
                    holder.unlockCanvasAndPost(canvas)
                }

                override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
                override fun surfaceDestroyed(holder: SurfaceHolder) {}
            })
        }
    }
}