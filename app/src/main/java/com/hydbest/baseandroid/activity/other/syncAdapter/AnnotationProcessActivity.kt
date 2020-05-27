package com.hydbest.baseandroid.activity.other.syncAdapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hydbest.baseandroid.R
import java.lang.Boolean
import java.lang.Short

class AnnotationProcessActivity :AppCompatActivity (){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annotation_process)
    }

    fun skip(view: View) {
        AnnoReceActivityBuilder.start(this,100,true,18,"csz")
    }

    @SuppressLint("UseValueOf")
    fun skipKt(view: View) {
        startAnnoReceActivity(1000, false, 100,"ccsszz")
    }


}