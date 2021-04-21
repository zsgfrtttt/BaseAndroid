package com.hydbest.baseandroid.activity.anim.motion

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.hydbest.baseandroid.R

class MotionActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion)

        val demoList: RecyclerView = findViewById(R.id.demo_list)
        val viewModel = viewModels<DemoListViewModel>().value

        val adapter = DemoListAdapter { demo ->
            startActivity(demo.toIntent())
        }
        demoList.adapter = adapter
        viewModel.demos.observe(this){
            adapter.submitList(it)
        }

    }
}