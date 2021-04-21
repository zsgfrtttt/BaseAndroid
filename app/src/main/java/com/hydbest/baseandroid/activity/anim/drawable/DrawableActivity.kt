package com.hydbest.baseandroid.activity.anim.drawable

import android.annotation.SuppressLint
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.SeekableAnimatedVectorDrawable
import com.hydbest.baseandroid.R
import kotlinx.android.synthetic.main.activity_md_button.view.*

class DrawableActivity :AppCompatActivity(){

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawable)

        val start = findViewById<Button>(R.id.start)
        val stop = findViewById<Button>(R.id.stop)
        val seek = findViewById<SeekBar>(R.id.seek)
        val iv = findViewById<ImageView>(R.id.icon)

        val icon = SeekableAnimatedVectorDrawable.create(
                this,
                R.drawable.ic_hourglass_animated
        )!!
        icon.registerAnimationCallback(object : SeekableAnimatedVectorDrawable.AnimationCallback() {

            override fun onAnimationStart(drawable: SeekableAnimatedVectorDrawable) {
                start.setText("pause")
                stop.isEnabled = true
            }

            override fun onAnimationPause(drawable: SeekableAnimatedVectorDrawable) {
                start.setText("resume")
            }

            override fun onAnimationResume(drawable: SeekableAnimatedVectorDrawable) {
                start.setText("pause")
            }

            override fun onAnimationEnd(drawable: SeekableAnimatedVectorDrawable) {
                start.setText("start")
                stop.isEnabled = false
            }

            override fun onAnimationUpdate(drawable: SeekableAnimatedVectorDrawable) {
                seek.progress = (seek.max * (drawable.currentPlayTime.toFloat() /
                        drawable.totalDuration.toFloat())).toInt()
            }
        })
        iv.setImageDrawable(icon)
        start.setOnClickListener {
            when {
                // You can pause and resume SeekableAnimatedVectorDrawable.
                icon.isPaused -> icon.resume()
                icon.isRunning -> icon.pause()
                else -> icon.start()
            }
        }
        stop.setOnClickListener { icon.stop() }
        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    // With SeekableAnimatedVectorDrawable#setCurrentPlayTime, you can set the
                    // position of animation to the specific time in its duration.
                    icon.currentPlayTime = (icon.totalDuration *
                            (progress.toFloat() / seekBar.max.toFloat())).toLong()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }
}