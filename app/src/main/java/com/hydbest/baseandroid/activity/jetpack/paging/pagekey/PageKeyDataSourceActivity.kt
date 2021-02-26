package com.hydbest.baseandroid.activity.jetpack.paging.pagekey

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.activity.jetpack.paging.CommonAdapter
import com.hydbest.baseandroid.activity.jetpack.room.Student

/**
 * 以具体的位置加载数据源固定的数据项
 */
class PageKeyDataSourceActivity : AppCompatActivity() {

    lateinit var rv:RecyclerView
    lateinit var model: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_positional_datasource)

        model = ViewModelProvider(this, SavedStateViewModelFactory(application, this)).get(ViewModel::class.java)
        rv = findViewById<RecyclerView>(R.id.rv)
        rv.setLayoutManager(LinearLayoutManager(this))
        val adapter = CommonAdapter()
        rv.adapter = adapter

        model.data.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    class ViewModel(application: Application, val handle: SavedStateHandle) : AndroidViewModel(application){

        val config:PagedList.Config by lazy {
            PagedList.Config.Builder().
            setEnablePlaceholders(true).setPageSize(20).setPrefetchDistance(3).setInitialLoadSizeHint(20).setMaxSize(Int.MAX_VALUE).build()
        }
        val data : LiveData<PagedList<Student>> by lazy {
            LivePagedListBuilder(PageKeyDataSourceFactory(),20).build()
        }
    }

}