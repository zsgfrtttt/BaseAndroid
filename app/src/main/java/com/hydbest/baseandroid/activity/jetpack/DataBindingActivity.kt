package com.hydbest.baseandroid.activity.jetpack

import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.hydbest.baseandroid.BR
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.activity.jetpack.binding.BtAdapter
import com.hydbest.baseandroid.activity.jetpack.room.Student
import com.hydbest.baseandroid.activity.jetpack.room.StudentDao
import com.hydbest.baseandroid.activity.jetpack.room.StudentDatabase
import com.hydbest.baseandroid.databinding.ActivityDataBindingBinding
import java.util.*
import kotlin.collections.ArrayList

class DataBindingActivity : AppCompatActivity() {

    val model: ViewModel by lazy { ViewModelProvider(this, SavedStateViewModelFactory(application, this)).get(ViewModel::class.java) }
    val binding: ActivityDataBindingBinding by lazy { DataBindingUtil.setContentView<ActivityDataBindingBinding>(this, R.layout.activity_data_binding) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.model = model
        model.getStudent().observe(this, Observer {
            binding.student = it[0]
            //  binding.setVariable(BR.student,it[1])
        })

        binding.imgUrl = model.url
        binding.imgResId = R.drawable.a

        binding.padding = 100 // 生命周期显示后才会调用对应的BindingAdapter方法

        //绑定
        binding.name = model.text
        Handler().postDelayed(Runnable {
            model.text.set("已经修改后")

            binding.alpha = 100
        }, 2000)
        Handler().postDelayed(Runnable {
            binding.tvName.text = "最终修改版本"
            Log.i("csz", "监听   ${model.text.get()}")
        }, 3000)
        Handler().postDelayed(Runnable {
            Log.i("csz", "监听   ${model.text.get()}")
        }, 4000)

        model.observe.observe(this, Observer {
            Log.i("csz", "observe")
            binding.padding = 210
        })


        val doubleBindable:DoubleBinding = DoubleBinding()
        binding.doubleBinding = doubleBindable

        //adapter绑定数据
        val list = mutableListOf<Student>()
        val adapter = BtAdapter(list).apply {
            list.add(Student("https://www.baidu.com/img/sug_bd.png?v=09816787.png",10))
            list.add(Student("https://www.baidu.com/img/sug_bd.png?v=09816787.png",20))
        }
        binding.rv.adapter =adapter

    }

    class ViewModel(application: Application, val handler: SavedStateHandle) : AndroidViewModel(application) {

        val text: ObservableField<String> = ObservableField("还未修改前")
        val url: ObservableField<String> = ObservableField("https://www.baidu.com/img/sug_bd.png?v=09816787.png")

        val observe: MutableLiveData<Any> = MutableLiveData(1)

        val studentDao: StudentDao by lazy {
            StudentDatabase.getInstance(getApplication()).studentDao
        }

        fun getStudent(): LiveData<List<Student>> {
            return studentDao.queryAll()
        }

        fun handleClick(text: String) {
            Toast.makeText(getApplication(), "this is a toast  $text", Toast.LENGTH_LONG).show()
        }

        fun handleViewClick(view: View) {
            Log.i("csz", "handleViewClick -  ${view.width}")
            observe.value = 2
        }

        fun changeText(tv: View) {
            (tv as TextView).text = ("textView")
            Log.i("csz", "changeText监听   ${text.get()}")
        }
    }

    //双向绑定
    class DoubleBinding : BaseObservable() {

        val student: Student by lazy { Student("yaoming", 45) }

        @Bindable
        fun getName(): String {
            Log.i("csz","getName")
            return student.name
        }

        fun setName(name: String) {
            Log.i("csz","setName   "  + name  + "    "  + student.name)
            if (!name.equals(student.name)){
                student.name = name
                notifyPropertyChanged(BR.doubleBinding)
            }
        }
    }

}