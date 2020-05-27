package com.hydbest.baseandroid.activity.Media

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csz.video.media.SlotValue
import com.csz.video.media.VideoHandler
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.adapter.VideoAdapter
import kotlinx.android.synthetic.main.activity_cus_video.*
import java.util.*

class MediaCusVideoActivity : AppCompatActivity() {
    private val mData: MutableList<SlotValue> = ArrayList()
    private var handle: VideoHandler? = null
    private var mIsList = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cus_video)
        initView()
        initEvent()
    }

    private fun initEvent() {
        btn_list.setOnClickListener { switchMode(true) }
        btn_single.setOnClickListener { switchMode(false) }
    }

    private fun initView() {
        switchMode(!mIsList)
    }

    override fun onBackPressed() {
        if (handle == null) {
            super.onBackPressed()
            return
        }
        if (handle!!.onBackPressed()) return
        super.onBackPressed()
    }

    override fun onPause() {
        if (handle != null) {
            handle!!.pause()
        }
        super.onPause()
    }

    private fun switchMode(list: Boolean) {
        if (mIsList == list) return
        mIsList = list
        if (mIsList) {
            rv.setVisibility(View.VISIBLE)
            lyout_top.setVisibility(View.GONE)
            for (i in 0..9) {
                mData.add(SlotValue("https://vd.yinyuetai.com/hd.yinyuetai.com/uploads/videos/common/90B8015D26C51713A86A1B985458D61E.mp4", null, false))
            }
            val adapter = VideoAdapter(mData)
            val manager = LinearLayoutManager(this)
            rv.setLayoutManager(manager)
            adapter.onAttachedToRecyclerView(rv)
        } else {
            rv.setVisibility(View.GONE)
            lyout_top.setVisibility(View.VISIBLE)
            handle = VideoHandler.handle(SlotValue("https://vd.yinyuetai.com/hd.yinyuetai.com/uploads/videos/common/90B8015D26C51713A86A1B985458D61E.mp4", null, false), lyout_top)
        }
    }
}