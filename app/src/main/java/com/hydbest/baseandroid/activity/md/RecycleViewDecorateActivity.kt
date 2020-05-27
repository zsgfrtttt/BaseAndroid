package com.hydbest.baseandroid.activity.md

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.adapter.TextAdapter
import com.hydbest.baseandroid.view.decoration.StickyDecoration
import kotlinx.android.synthetic.main.activity_recycle_decorate.*

/**
 * Created by csz on 2019/6/10.
 */
class RecycleViewDecorateActivity : AppCompatActivity() {
    private var adapter: TextAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_decorate)
        initView()
    }

    private fun initView() {
        adapter = TextAdapter(null)
        rv.setLayoutManager(LinearLayoutManager(this))
        rv.addItemDecoration(StickyDecoration())
        rv.setAdapter(adapter)
    }
}