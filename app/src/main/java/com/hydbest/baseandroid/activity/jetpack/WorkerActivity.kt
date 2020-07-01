package com.hydbest.baseandroid.activity.jetpack

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import com.hydbest.baseandroid.R
import kotlinx.android.synthetic.main.activity_worker.*

class WorkerActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker)

        val manager:WorkManager = WorkManager.getInstance(this)
        val constratin = Constraints.Builder().setRequiredNetworkType(NetworkType.NOT_REQUIRED).build()
        val request = OneTimeWorkRequestBuilder<MyWorker>()
                .setConstraints(constratin)
                .setInputData(workDataOf("key" to "value"))
                .build()
        btn.setOnClickListener{
            manager.enqueue(request)
        }

        val live = manager.getWorkInfoByIdLiveData(request.id)
        live.observe(this, Observer {
            it.outputData.run {
                val text = getString("data")
                text?.run {Log.i("csz",this)}
            }
        })
    }


    class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
        override fun doWork(): Result {
            val text = inputData.getString("key")
            Log.i("csz",text)
            Thread.sleep(2000)
            return Result.success(workDataOf("data" to "data"))
        }

    }
}