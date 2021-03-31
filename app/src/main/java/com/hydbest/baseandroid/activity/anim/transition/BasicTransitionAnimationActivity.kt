package com.hydbest.baseandroid.activity.anim.transition

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Scene
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.hydbest.baseandroid.R

class BasicTransitionAnimationActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener {

    private lateinit var mScene1: Scene
    private lateinit var mScene2: Scene
    private lateinit var mScene3: Scene

    private lateinit var mSceneRoot: FrameLayout
    private lateinit var mTransitionManagerForScene3: TransitionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_transition_anim)

        findViewById<RadioGroup>(R.id.select_scene).setOnCheckedChangeListener(this)
        mSceneRoot = findViewById(R.id.scene_root)

        mScene1 = Scene(mSceneRoot, mSceneRoot.findViewById(R.id.container) as ViewGroup)
        mScene2 = Scene.getSceneForLayout(mSceneRoot, R.layout.scene2, this)
        mScene3 = Scene.getSceneForLayout(mSceneRoot, R.layout.scene3, this)
        mTransitionManagerForScene3 = TransitionInflater.from(this)
                .inflateTransitionManager(R.transition.scene3_transition_manager, mSceneRoot)
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.select_scene_1 -> {
                TransitionManager.go(mScene1)
            }
            R.id.select_scene_2 -> {
                TransitionManager.go(mScene2)
            }
            R.id.select_scene_3 -> {
                mTransitionManagerForScene3.transitionTo(mScene3)
            }
            R.id.select_scene_4 -> {
                //在没有scene的情况下开启Transition
                TransitionManager.beginDelayedTransition(mSceneRoot)
                //更改试图属性
                val square = mSceneRoot.findViewById<View>(R.id.transition_square)
                val params = square.layoutParams
                val newSize = resources.getDimensionPixelSize(R.dimen.square_size_expanded)
                params.width = newSize
                params.height = newSize
                square.layoutParams = params
            }
        }
    }
}