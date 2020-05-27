package com.hydbest.baseandroid.activity.md

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.animation.BaseAnimation
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.adapter.TextAdapter
import kotlinx.android.synthetic.main.activity_recycle_anim.*

/**
 * Created by csz on 2019/5/22.
 */
class RecycleViewAnimActivity : AppCompatActivity() {
    companion object {
        private const val DEFAULT_SCALE_FROM = .5f
    }

    var adapter: TextAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_anim)
        initView()
        initEvent()
    }

    private fun initEvent() {
        btn_reset.setOnClickListener {
            adapter = TextAdapter(null)
            rv.setAdapter(adapter)
            rv.setAdapter(adapter)
            rv.startLayoutAnimation() }
        btn_add.setOnClickListener {
            adapter!!.addData(TextAdapter.Bean(adapter!!.itemCount.toString() + "", ""))
            rv.scrollToPosition(adapter!!.itemCount - 1)
        }
        btn_remove.setOnClickListener {
            adapter!!.remove(adapter!!.itemCount - 1)
        }
    }

    private fun initView() {
        adapter = TextAdapter(null)
        rv.setLayoutManager(LinearLayoutManager(this))
        rv.setAdapter(adapter)
        val animation = AnimationUtils.loadAnimation(this, R.anim.adapter_item_enter)
        val lac = LayoutAnimationController(animation)
        lac.order = LayoutAnimationController.ORDER_NORMAL
        lac.delay = 0.1f
        rv.setLayoutAnimation(lac)
    }

    inner class ScaleInAnimation @JvmOverloads constructor(private val mFrom: Float = Companion.DEFAULT_SCALE_FROM) : BaseAnimation {

        override fun animators(view: View): Array<Animator> {
            val scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f)
            val scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f)
            return arrayOf(scaleX, scaleY)
        }



    }
}