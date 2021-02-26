package com.hydbest.baseandroid.activity.jetpack.paging.itemkey

import androidx.paging.DataSource
import com.hydbest.baseandroid.activity.jetpack.room.Student

class ItemKeyDataSourceFactory : DataSource.Factory<Int, Student>() {
    override fun create(): DataSource<Int, Student> {
        val source: DataSource<Int, Student> = RealItemKeyDataSource()
        return source
    }
}