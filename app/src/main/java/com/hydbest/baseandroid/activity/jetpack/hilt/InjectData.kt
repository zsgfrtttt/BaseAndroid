package com.hydbest.baseandroid.activity.jetpack.hilt

import android.util.Log
import javax.inject.Inject

class InjectData @Inject constructor(val service: InjectService){
    fun print(){
        Log.i("csz","this is InjectData ${service}")
    }
}