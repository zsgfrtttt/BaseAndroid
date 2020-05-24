package com.hydbest.baseandroid.activity.jetpack

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.hydbest.baseandroid.R
import kotlinx.android.synthetic.main.activity_kt.*
import kotlinx.android.synthetic.main.item_shadow.*
import kotlinx.coroutines.*

class KtActivity :AppCompatActivity() {

    var model:MyViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kt)
     //   setContentView(R.layout.activity_csz)

      //  lifecycleScope.launch { el() }
       // val model by viewModels<MyViewModel>()
        model = viewModels<MyViewModel>().value

        model?.getData()?.observe(this,object :Observer<Int> {
            override fun onChanged(t: Int?) {
               btn.setText(t.toString())
            }

        })
    }

    fun test(i:Int):Any{
        return 0;
    }

    fun add(view: View) {
        model?.add()
    }

    suspend fun el(){
        withContext(Dispatchers.IO){
            delay(5000)
            MainScope().launch {
               // Toast.makeText()
                Toast.makeText(this@KtActivity,"hahah ",Toast.LENGTH_LONG).show()
            }
        }
    }

    class MyViewModel(val handle:SavedStateHandle): ViewModel(){
        val key = "key"
        fun add(){

            handle.getLiveData(key,0).value =  handle.getLiveData(key,0).value!!.plus(1)
            Log.i("csz","v " +handle.getLiveData(key,0).value)
        }

        fun getData():LiveData<Int>{
            return  handle.getLiveData(key,0)
        }
    }




}