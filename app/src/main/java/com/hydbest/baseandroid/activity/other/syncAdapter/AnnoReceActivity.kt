package com.hydbest.baseandroid.activity.other.syncAdapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.csz.annotation.Builder
import com.csz.annotation.Optional
import com.csz.annotation.Required
import com.hydbest.baseandroid.R
import kotlinx.android.synthetic.main.activity_anno_rece.*
import java.util.*
import kotlin.collections.ArrayList

@Builder
class AnnoReceActivity: AppCompatActivity(){

    @Required
    var age:Int = 0

    @Optional
    lateinit var name:String
    @Optional
    var level: Short = 0
    @Optional
    var length:Boolean = false
    @Optional
    lateinit var list: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anno_rece)
        tvage.setText(age.toString())
        tvname.setText(name)
        tvlevel.setText(level.toString())
        tvlength.setText(length.toString())
        tvlist.setText(Arrays.toString(list.toArray()))
    }


}