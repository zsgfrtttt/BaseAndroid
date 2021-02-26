package com.hydbest.baseandroid.activity.jetpack.paging.itemkey

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.activity.jetpack.paging.CommonAdapter
import com.hydbest.baseandroid.activity.jetpack.paging.boundary.Callack
import com.hydbest.baseandroid.activity.jetpack.room.Student
import com.hydbest.baseandroid.activity.jetpack.room.StudentDatabase
import java.util.ArrayList
import kotlin.concurrent.thread

/**
 * 以具体的位置加载数据源固定的数据项
 */
class BoundaryActivity : AppCompatActivity() {

    lateinit var rv: RecyclerView
    lateinit var refresh: SwipeRefreshLayout
    lateinit var model: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_positional_datasource)

        model = ViewModelProvider(this, SavedStateViewModelFactory(application, this)).get(ViewModel::class.java)

        refresh = findViewById(R.id.refresh)
        refresh.setOnRefreshListener {
            StudentDatabase.getInstance(getApplication()).studentDao.deleteAll()
            refresh.isRefreshing = false
        }

        rv = findViewById(R.id.rv)
        rv.setLayoutManager(LinearLayoutManager(this))
        val adapter = CommonAdapter()
        rv.adapter = adapter


        //预先设置数据
        var stus:ArrayList<Student> = ArrayList()
        for (index in 1..50){
            stus.add(Student("pre name $index" ,index))
        }
        StudentDatabase.getInstance(this).studentDao.deleteAll()
        StudentDatabase.getInstance(this).studentDao.insertList(stus)
        val queryList = StudentDatabase.getInstance(this).studentDao.queryList()
        queryList.forEach {
            Log.i("csz","stu   $it")
        }


        model.data.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    class ViewModel(application: Application, val handle: SavedStateHandle) : AndroidViewModel(application) {

        val config: PagedList.Config by lazy {
            PagedList.Config.Builder().setEnablePlaceholders(true).setPageSize(20).setPrefetchDistance(3).setInitialLoadSizeHint(20).setMaxSize(Int.MAX_VALUE).build()
        }

        val data: LiveData<PagedList<Student>> by lazy {
            LivePagedListBuilder(StudentDatabase.getInstance(getApplication()).studentDao.queryAllData(), 20)
                    .setBoundaryCallback(Callack(getApplication()))
                    .build()
        }
    }

}