package com.hydbest.baseandroid.activity.jetpack.paging.itemkey

import android.util.Log
import androidx.paging.ItemKeyedDataSource
import com.hydbest.baseandroid.activity.jetpack.paging.DataFactory
import com.hydbest.baseandroid.activity.jetpack.room.Student

open class RealItemKeyDataSource() : ItemKeyedDataSource<Int, Student>() {

    override fun getKey(item: Student): Int {
        return item.id  //每次结尾的item
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Student>) {
        callback.onResult(DataFactory.fetchData(0, 20))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Student>) {
        Log.i("csz", "RealItemKeyDataSource  loadAfter   " + params.key + "    " + params.requestedLoadSize)
        callback.onResult(DataFactory.fetchData((params.key + 1), 20))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Student>) {

    }


}