package com.hydbest.baseandroid.activity.other

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.listener.OnItemLongClickListener
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.adapter.ExpandableItemAdapter
import com.hydbest.baseandroid.entity.Fragmentation
import com.hydbest.baseandroid.util.DataGeneration
import kotlinx.android.synthetic.main.activity_android_advance.*

/**
 * Created by csz on 2018/11/6.
 */
class AndroidAdvanceActivity : AppCompatActivity() {
    var adapter: ExpandableItemAdapter? = null
    var list: List<BaseNode>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_advance)
        list = DataGeneration.getSourceItems()
        adapter = ExpandableItemAdapter(list)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        adapter!!.setOnItemLongClickListener(OnItemLongClickListener { adapter, view, position ->
            if (this@AndroidAdvanceActivity.adapter!!.getItemViewType(position) == ExpandableItemAdapter.TYPE_LEVEL_1) {
                val fragmentation = adapter.getItem(position) as Fragmentation
                val uri = Uri.parse(fragmentation.content.split("-").toTypedArray()[1])
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
                val test = Test()
                test.main()
                return@OnItemLongClickListener true
            }
            false
        })
    }

    class Test {
        open class A {
            private fun eat() {
                Log.i("csz", "eeeeeeeeeeeeeee   " + this.javaClass.name)
            }
        }

        class B : A()
        class C {
            private fun eat() {
                Log.i("csz", "cccccccc   " + this.javaClass.name)
            }
        }

        fun main() {
            try {
                val eat = A::class.java.getDeclaredMethod("eat")
                eat.isAccessible = true
                val instance = B::class.java.newInstance()
                eat.invoke(instance)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("csz", "!!!!!!!   " + e.message)
            }
        }
    }
}