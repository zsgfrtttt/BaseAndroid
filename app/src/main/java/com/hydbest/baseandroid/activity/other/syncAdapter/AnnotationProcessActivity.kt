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
        val list:ArrayList<String> = ArrayList()
        list.addAll(arrayOf("caishuzhan","is","handsome"))
        AnnoReceActivityBuilder.start(this,100,true,18,list,"csz")
    }

    @SuppressLint("UseValueOf")
    fun skipKt(view: View) {
        val list:java.util.ArrayList<java.lang.String> = java.util.ArrayList<java.lang.String>()
        list.add(java.lang.String("caishuzhan"))
        list.add(java.lang.String("is"))
        list.add(java.lang.String("very"))
        list.add(java.lang.String("handsome"))
        startAnnoReceActivity(1000, false, 100,list,"ccsszz")
    }


}