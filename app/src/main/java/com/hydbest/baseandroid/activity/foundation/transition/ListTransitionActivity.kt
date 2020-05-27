package com.hydbest.baseandroid.activity.foundation.transition

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.adapter.TextAdapter
import kotlinx.android.synthetic.main.activity_list_transition.*

/**
 * Created by csz on 2018/11/5.
 */
class ListTransitionActivity : AppCompatActivity() {
    var mTextAdapter: TextAdapter? = null

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_transition)
        mTextAdapter = TextAdapter(null)
        rv.setLayoutManager(LinearLayoutManager(this))
        rv.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        mTextAdapter!!.onAttachedToRecyclerView(rv)
        initEvent()
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private fun initEvent() {
        mTextAdapter!!.setOnItemChildClickListener { adapter, view, position -> start(adapter.getViewByPosition(position, R.id.iv)) }
        iv.setOnClickListener(View.OnClickListener { v -> start(v) })
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private fun start(view: View?) {
        val intent = Intent(this, DetailTransitionActivity::class.java)
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view!!, "userAvatar")
        startActivity(intent, optionsCompat.toBundle())
    }

    /**
     * 扩散效果
     *
     * @param view
     */
    fun createCircularReveal(view: View?) {
        val cx: Int = root.getWidth() / 2
        val cy: Int = root.getHeight() / 2
        val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
        var anim: Animator? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(root, cx, cy, 0f, finalRadius).setDuration(5000)
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                }
            })
            anim.start()
        }
    }
}