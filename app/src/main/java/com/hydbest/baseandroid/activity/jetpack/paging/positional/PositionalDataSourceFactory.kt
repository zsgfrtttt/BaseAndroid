package com.hydbest.baseandroid.activity.jetpack.paging.positional

import androidx.paging.DataSource
import com.hydbest.baseandroid.activity.jetpack.room.Student

class PositionalDataSourceFactory : DataSource.Factory<Int, Student>() {
    override fun create(): DataSource<Int, Student> {
        val source: DataSource<Int, Student> = RealPositionalDataSource()
        return source
    }
}