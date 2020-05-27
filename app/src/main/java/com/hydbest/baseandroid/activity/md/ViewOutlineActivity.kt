package com.hydbest.baseandroid.activity.md

import android.graphics.Color
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hydbest.baseandroid.R
import kotlinx.android.synthetic.main.activity_view_outline.*

/**
 * Created by csz on 2019/5/23.
 */
class ViewOutlineActivity : AppCompatActivity(), OnSeekBarChangeListener {
    var isStart = false
    private var startWidth = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_outline)
        seek_ele.setOnSeekBarChangeListener(this)
        seek_z.setOnSeekBarChangeListener(this)
        seek_coner.setOnSeekBarChangeListener(this)
        seek_coner.post(Runnable {
            val radius: Int = v_coner.getHeight() / 2
            setDrawable(radius)
        })

        btn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                iv.setOutlineProvider(object : ViewOutlineProvider() {
                    override fun getOutline(view: View, outline: Outline) {
                        val margin = Math.min(view.width, view.height) / 10
                        //阴影的透明度
                        outline.alpha = 0.5f
                        outline.setRoundRect(margin, margin, view.width - margin, view.height - margin, (view.width - 2 * margin) / 2.toFloat())
                    }
                })
                iv.setClipToOutline(true)
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (seekBar === seek_ele) {
            iv.setElevation(progress.toFloat())
        } else if (seekBar === seek_z) {
            iv.setTranslationZ(progress.toFloat())
        } else {
            if (!isStart) {
                startWidth = v_coner.getWidth()
                isStart = true
            }
            val offset = progress * 200 / seekBar.max
            val params: ViewGroup.LayoutParams = v_coner.getLayoutParams()
            params.width = startWidth + offset
            v_coner.setLayoutParams(params)
            val radius: Int = v_coner.getHeight() / 2
            val outRadius = radius * (seekBar.max - progress) / seekBar.max
            setDrawable(outRadius)
        }
    }

    private fun setDrawable(outRadius: Int) {
        val outerRadii = floatArrayOf(outRadius.toFloat(), outRadius.toFloat(), outRadius.toFloat(), outRadius.toFloat(), outRadius.toFloat(), outRadius.toFloat(), outRadius.toFloat(), outRadius.toFloat())
        val innerRadii = floatArrayOf(outRadius.toFloat(), outRadius.toFloat(), outRadius.toFloat(), outRadius.toFloat(), outRadius.toFloat(), outRadius.toFloat(), outRadius.toFloat(), outRadius.toFloat())
        val roundRectShape = RoundRectShape(outerRadii, null, innerRadii)
        val drawable = ShapeDrawable(roundRectShape)
        drawable.paint.color = Color.RED
        drawable.paint.style = Paint.Style.FILL
        v_coner.setBackgroundDrawable(drawable)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}