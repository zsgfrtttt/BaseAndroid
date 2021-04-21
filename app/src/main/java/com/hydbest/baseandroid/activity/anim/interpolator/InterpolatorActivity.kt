package com.hydbest.baseandroid.activity.anim.interpolator

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Path
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hydbest.baseandroid.R

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class InterpolatorActivity : AppCompatActivity() {

    private val mView: View by lazy { findViewById<View>(R.id.square) }

    private val mInterpolatorSpinner: Spinner by lazy { findViewById<Spinner>(R.id.interpolatorSpinner) }

    private val mDurationSeekbar: SeekBar by lazy { findViewById<SeekBar>(R.id.durationSeek); }

    private val mDurationLabel: TextView by lazy { findViewById<TextView>(R.id.durationLabel); }

    private val mInterpolators: Array<Interpolator> by lazy {
        arrayOf(AnimationUtils.loadInterpolator(this, android.R.interpolator.linear),
                AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_linear_in),
                AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in),
                AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in))
    }

    private val mPathIn: Path by lazy { Path() }.apply {
        value.moveTo(0.2f, 0.2f)
        value.lineTo(1f, 1f)
    }

    private val mPathOut: Path by lazy { Path() }.apply {
        value.moveTo(1f, 1f)
        value.lineTo(0.2f, 0.2f)
    }

    private var mIsOut = false

    private val INITIAL_DURATION_MS = 750

    val TAG = "InterpolatorPlaygroundFragment"

    private val mInterpolatorNames: Array<String> by lazy { getResources().getStringArray(R.array.interpolator_names) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interpolator)

        initAnimateButton()

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mInterpolatorNames)
        mInterpolatorSpinner.adapter = spinnerAdapter

        initSeekbar()
    }

    @SuppressLint("LongLogTag")
    private fun initAnimateButton() {
        val button = findViewById<View>(R.id.animateButton)
        button.setOnClickListener { // Interpolator selected in the spinner
            val selectedItemPosition = mInterpolatorSpinner.selectedItemPosition
            val interpolator = mInterpolators[selectedItemPosition]
            // Duration selected in SeekBar
            val duration = mDurationSeekbar.progress.toLong()
            // Animation path is based on whether animating in or out
            val path = if (mIsOut) mPathIn else mPathOut

            // Log animation details
            Log.i(TAG, String.format("Starting animation: [%d ms, %s, %s]",
                    duration, mInterpolatorSpinner.selectedItem as String,
                    if (mIsOut) "Out (growing)" else "In (shrinking)"))

            // Start the animation with the selected options
            startAnimation(interpolator, duration, path)

            // Toggle direction of animation (path)
            mIsOut = !mIsOut
        }
    }

    private fun initSeekbar() {
        // Register listener to update the text label when the SeekBar value is updated
        mDurationSeekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                mDurationLabel.text = resources.getString(R.string.animation_duration, i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Set initial progress to trigger SeekBarChangeListener and update UI
        mDurationSeekbar.progress = INITIAL_DURATION_MS
    }

    /**
     * Start an animation on the sample view.
     * The view is animated using an [android.animation.ObjectAnimator] on the
     * [View.SCALE_X] and [View.SCALE_Y] properties, with its animation based on a
     * path.
     * The only two paths defined here ([.mPathIn] and [.mPathOut]) scale the view
     * uniformly.
     *
     * @param interpolator The interpolator to use for the animation.
     * @param duration Duration of the animation in ms.
     * @param path Path of the animation
     * @return The ObjectAnimator used for this animation
     * @see android.animation.ObjectAnimator.ofFloat
     */
    fun startAnimation(interpolator: Interpolator?, duration: Long, path: Path?): ObjectAnimator? {
        // This ObjectAnimator uses the path to change the x and y scale of the mView object.
        val animator = ObjectAnimator.ofFloat(mView, View.SCALE_X, View.SCALE_Y, path)

        // Set the duration and interpolator for this animation
        animator.duration = duration
        animator.interpolator = interpolator
        animator.start()
        return animator
    }

}

