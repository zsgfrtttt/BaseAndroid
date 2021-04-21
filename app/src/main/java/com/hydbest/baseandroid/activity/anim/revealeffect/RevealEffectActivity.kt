package com.hydbest.baseandroid.activity.anim.revealeffect

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import com.hydbest.baseandroid.R

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class RevealEffectActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reveal_effect)
        init()
    }

    private fun init() {
        val shape: View = findViewById(R.id.circle)
        val button: View = findViewById(R.id.button)
        // Set a listener to reveal the view when clicked.
        // Set a listener to reveal the view when clicked.
        button.setOnClickListener { // Create a reveal {@link Animator} that starts clipping the view from
            // the top left corner until the whole view is covered.
            val circularReveal = ViewAnimationUtils.createCircularReveal(
                    shape,
                    0,
                    0, 0f,
                    Math.hypot(shape.width.toDouble(), shape.height.toDouble()).toFloat())
            circularReveal.interpolator = AccelerateDecelerateInterpolator()
            // Finally start the animation
            circularReveal.start()
            Log.d("csz", "Starting Reveal animation")
        }
    }
}