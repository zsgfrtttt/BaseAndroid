package com.hydbest.baseandroid.activity.jetpack.hilt

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.csz.video.media.SlotValue
import com.hydbest.baseandroid.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HiltActivity:AppCompatActivity() {

    @Inject
    lateinit var injectData:InjectData

    @Inject
    lateinit var anim:Animal

    @Inject
    lateinit var slotValue: SlotValue

    @AnnoCat
    @Inject
    lateinit var cat:Animal

    @Inject
    lateinit var adapter:InjectAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hilt)

        injectData.print()
        anim.eat()
        cat.eat()
        adapter.test()
        Log.i("csz","slot ${slotValue}")
    }
}