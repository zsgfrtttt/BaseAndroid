package com.hydbest.baseandroid.activity.foundation

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.activity.md.MD_ButtonActivity
import com.hydbest.baseandroid.adapter.TextAdapter
import kotlinx.android.synthetic.main.activity_context_menu.*

/**
 * Created by csz on 2018/11/5.
 */
class ContextMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_context_menu)
        registerForContextMenu(rv)
        registerForContextMenu(tv)
        rv.setLayoutManager(LinearLayoutManager(this))
        rv.setAdapter(TextAdapter(null))
    }

    /**
     * @param menuInfo
     * ViewGroup.showContextMenuForChild(View originalView)
     * View.getContextMenuInfo()
     */
    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.setHeaderTitle("title")
        menu.add("sub1").intent = Intent(this, MD_ButtonActivity::class.java)
        menu.add("sub2").intent = Intent(this, MD_ButtonActivity::class.java)
        menu.add("sub3").intent = Intent(this, MD_ButtonActivity::class.java)
    }
}