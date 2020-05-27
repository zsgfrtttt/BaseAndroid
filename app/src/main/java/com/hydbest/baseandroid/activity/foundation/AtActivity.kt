package com.hydbest.baseandroid.activity.foundation

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.activity.integrate.span.DataBindingSpan
import com.hydbest.baseandroid.activity.integrate.span.impl.SpannableData
import com.hydbest.baseandroid.helper.KeyCodeDeleteHelper
import kotlinx.android.synthetic.main.activity_at.*

/**
 * Created by csz on 2018/12/20.
 */
class AtActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_at)
        et.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                KeyCodeDeleteHelper.onDelDown(et.getText())
            } else false
        })
        //取数据
        val spans: Array<DataBindingSpan<*>> = et.getText().getSpans(0, et.getText().length, DataBindingSpan::class.java)
        if (spans != null) {
            for (span in spans) {
                Log.i("csz", "spans:" + span.bindingData())
            }
        }
    }

    fun add(view: View?) {
        val span = SpannableData("caishuzhan")
        et.getText().append(span.spannedText())
    }

    fun less(view: View?) {}
}