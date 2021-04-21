package com.hydbest.baseandroid.activity.anim.transition

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Scene
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.hydbest.baseandroid.R

class CustomTransitionActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mScenes: Array<Scene>
    private lateinit var mTransition: Transition
    private var mCurrentScene :Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_transition)

        val container = findViewById<FrameLayout>(R.id.container)
        findViewById<Button>(R.id.show_next_scene).setOnClickListener(this)
        mScenes = arrayOf(
                Scene.getSceneForLayout(container, R.layout.cus_scene1, this),
                Scene.getSceneForLayout(container, R.layout.cus_scene2, this),
                Scene.getSceneForLayout(container, R.layout.cus_scene3, this))

        mTransition = ChangeColor()
        TransitionManager.go(mScenes[mCurrentScene % mScenes.size])
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.show_next_scene -> {
                mCurrentScene = (mCurrentScene + 1) % mScenes.size
                Log.i("csz", "Transitioning to scene #$mCurrentScene")
                // Pass the custom Transition as second argument for TransitionManager.go
                TransitionManager.go(mScenes[mCurrentScene], mTransition)
            }
        }
    }
}