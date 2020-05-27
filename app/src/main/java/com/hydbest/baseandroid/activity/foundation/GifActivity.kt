package com.hydbest.baseandroid.activity.foundation

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.gifdecoder.GifDecoder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.activity.other.AndroidAdvanceActivity
import kotlinx.android.synthetic.main.activity_gif.*

/**
 * Created by csz on 2018/11/22.
 */
class GifActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif)
        loadGif()
    }

    private fun loadGif() {
        Glide.with(this).load(R.drawable.icon_gif).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                // 计算动画时长
                val drawable = resource as GifDrawable
                resource.setLoopCount(1)
                try {
                    val stateField = resource.javaClass.getDeclaredField("state")
                    stateField.isAccessible = true
                    val gifStateClass: Class<*> = stateField[drawable].javaClass
                    val frameLoaderField = gifStateClass.getDeclaredField("frameLoader")
                    frameLoaderField.isAccessible = true
                    val frameLoaderObj = frameLoaderField[stateField[drawable]]
                    val gifDecoder = frameLoaderObj.javaClass.getDeclaredField("gifDecoder")
                    gifDecoder.isAccessible = true
                    val decoder = gifDecoder[frameLoaderObj] as GifDecoder

                    //  GifDecoder decoder = drawable.startFromFirstFrame();
                    var duration: Long = 0
                    for (i in 0 until drawable.frameCount) {
                        duration += decoder.getDelay(i).toLong()
                    }
                    Handler(Looper.getMainLooper()).postDelayed({ startActivity(Intent(this@GifActivity, AndroidAdvanceActivity::class.java)) }, duration)
                } catch (e: NoSuchFieldException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                return false
            }
        }).into(iv)
    }
}