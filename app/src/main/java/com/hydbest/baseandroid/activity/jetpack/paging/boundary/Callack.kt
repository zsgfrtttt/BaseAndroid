package com.hydbest.baseandroid.activity.jetpack.paging.boundary

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.paging.PagedList
import com.hydbest.baseandroid.activity.jetpack.paging.DataFactory
import com.hydbest.baseandroid.activity.jetpack.room.Student
import com.hydbest.baseandroid.activity.jetpack.room.StudentDao
import com.hydbest.baseandroid.activity.jetpack.room.StudentDatabase
import kotlin.concurrent.thread

class Callack(val application: Application) : PagedList.BoundaryCallback<Student>() {

    val dao: StudentDao by lazy { StudentDatabase.getInstance(application).studentDao }

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        //模拟网络请求数据
        thread {
            val list = DataFactory.fetchData(0, 20)
            dao.insertList(list)
            Log.i("csz"," after  onZeroItemsLoaded  size    "  + StudentDatabase.getInstance(application).studentDao.queryList().size)
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: Student) {
        super.onItemAtFrontLoaded(itemAtFront)
        Log.i("csz","   callback   onItemAtFrontLoaded  size       ${itemAtFront}     ")
    }

    override fun onItemAtEndLoaded(itemAtEnd: Student) {
        super.onItemAtEndLoaded(itemAtEnd)
        thread {
            val list = DataFactory.fetchData(itemAtEnd.age + 1, 20)

            Log.i("csz","${itemAtEnd.age +  1}    after  onItemAtEndLoaded  size    "  + StudentDatabase.getInstance(application).studentDao.queryList().size)
            dao.insertList(list)
        }
    }

}