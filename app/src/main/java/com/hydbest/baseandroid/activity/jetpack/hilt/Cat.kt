package com.hydbest.baseandroid.activity.jetpack.hilt

import android.util.Log
import javax.inject.Inject

class Cat @Inject constructor():Animal {
    override fun eat() {
        Log.i("csz","this is a cat to eat")
    }
}