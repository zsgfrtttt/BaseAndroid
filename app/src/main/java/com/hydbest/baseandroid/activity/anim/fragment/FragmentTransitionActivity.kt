package com.hydbest.baseandroid.activity.anim.fragment

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hydbest.baseandroid.R

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class FragmentTransitionActivity : AppCompatActivity() {


    companion object {
        var curPosition: Int = 0
        val KEY = "key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_transition)
        savedInstanceState?.let {
            curPosition = it.getInt(KEY, 0)
            return
        }
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, GridFragment(), GridFragment::class.java.simpleName)
                .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY, curPosition)
    }
}