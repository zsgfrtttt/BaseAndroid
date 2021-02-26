package com.hydbest.baseandroid.activity.jetpack.paging.pagekey

import androidx.paging.DataSource
import com.hydbest.baseandroid.activity.jetpack.room.Student

class PageKeyDataSourceFactory : DataSource.Factory<Int, Student>() {
    override fun create(): DataSource<Int, Student> {
        val source: DataSource<Int, Student> = RealPageKeyDataSource()
        return source
    }
}