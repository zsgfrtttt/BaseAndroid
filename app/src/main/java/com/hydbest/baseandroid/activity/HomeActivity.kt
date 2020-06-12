package com.hydbest.baseandroid.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.node.BaseNode
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.adapter.ExpandableItemAdapter
import com.hydbest.baseandroid.util.DataGeneration
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    var adapter: ExpandableItemAdapter? = null
    var list: List<BaseNode>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()

        // startActivity();
    }

    private fun initView() {
        list = DataGeneration.getItems()
        adapter = ExpandableItemAdapter(list)
        val manager = GridLayoutManager(this, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (adapter!!.getItemViewType(position) == BaseQuickAdapter.LOAD_MORE_VIEW) {
                    return manager.spanCount
                }
                return if (adapter!!.getItemViewType(position) == ExpandableItemAdapter.TYPE_LEVEL_0) manager.spanCount else 1
            }
        }
        rv.adapter = adapter
        rv.layoutManager = manager
        //adapter.expandAll();
    }

    companion object{

        @JvmStatic
        fun main(args:Array<String>){
           val btn :ArrayList<Button> = ArrayList()
            sequenceOf(1,2,3,4).forEach {
                println("kk $it")
            }
        }

    }
}