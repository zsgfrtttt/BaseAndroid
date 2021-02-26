package com.hydbest.baseandroid.activity.jetpack.paging.pagekey

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.hydbest.baseandroid.activity.jetpack.paging.DataFactory
import com.hydbest.baseandroid.activity.jetpack.room.Student

open class RealPageKeyDataSource() : PageKeyedDataSource<Int, Student>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Student>) {
        callback.onResult(DataFactory.fetchData(0, 20), 0, 1)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Student>) {}

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Student>) {
        Log.i("csz", "loadAfter   ${params.key}      ${params.requestedLoadSize}")
        Thread.sleep(3000)
        callback.onResult(DataFactory.fetchData(params.key * params.requestedLoadSize, params.requestedLoadSize), params.key + 1)
    }

}