package com.hydbest.baseandroid.activity.jetpack.paging.positional

import android.util.Log
import androidx.paging.PositionalDataSource
import com.hydbest.baseandroid.activity.jetpack.paging.DataFactory
import com.hydbest.baseandroid.activity.jetpack.room.Student

open class RealPositionalDataSource() :PositionalDataSource<Student>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Student>) {
        callback.onResult(DataFactory.fetchData(0, 20),0 ,100)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Student>) {
        Log.i("csz","startPos  " + params.startPosition + "     loadSize     "  + params.loadSize)
        Thread.sleep(5000)
        callback.onResult(DataFactory.fetchData(params.startPosition, params.loadSize))
    }

}