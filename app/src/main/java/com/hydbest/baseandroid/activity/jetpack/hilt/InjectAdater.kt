package com.hydbest.baseandroid.activity.jetpack.hilt

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class InjectAdapter @Inject constructor(@ActivityContext private val context: Context) {

    fun test(){
        Log.i("csz","this is a test fun by InjectAdapter")
    }
}