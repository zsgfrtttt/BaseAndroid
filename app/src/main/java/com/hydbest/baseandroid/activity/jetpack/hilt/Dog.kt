package com.hydbest.baseandroid.activity.jetpack.hilt

import android.util.Log
import javax.inject.Inject

class Dog @Inject constructor():Animal {
    override fun eat() {
        Log.i("csz","this is a dog to eat")
    }
}