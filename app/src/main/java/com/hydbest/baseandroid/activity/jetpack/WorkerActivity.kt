package com.hydbest.baseandroid.activity.jetpack

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import com.hydbest.baseandroid.R
import kotlinx.android.synthetic.main.activity_worker.*
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * WorkerManager实际上是API >= 23 采用JobScheduler ，< 23 采用 AlarmManager + BroadcastReceiver的方式
 * 应用彻底退出或者重启后能否自动执行与设备有关（系统商对底层有修改，可能无法唤起AlarmManager或者JobScheduler），原生系统可以自动执行
 */
class WorkerActivity : AppCompatActivity() {
    @SuppressLint("EnqueueWork")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker)

        val manager: WorkManager = WorkManager.getInstance(this)
        //任务的触发条件
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.NOT_REQUIRED).build()

        /**
         * 选择任务类型
         * @see OneTimeWorkRequest 一次性任务
         * @see PeriodicWorkRequest 周期性任务，且任务间隔不能少于15分钟
         */
        val request = OneTimeWorkRequestBuilder<MyWorker>()
                .setConstraints(constraints)
                .setInputData(workDataOf("key" to "value"))
                .setInitialDelay(10, TimeUnit.MINUTES)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.MINUTES) //假如Worker返回retry则会根据策略重试
                .addTag("tag") //方便跟踪和取消任务
                .build()
        /**
         *   提交执行任务
         *   @code
         *   val workContinuation1 = manager.beginWith(request).then(request);
         *   val workContinuation2 = manager.beginWith(request).then(request);
         *   WorkContinuation.combine(arrayListOf(workContinuation1,workContinuation2)).enqueue()
         */
        btn.setOnClickListener {
            manager.enqueue(request)
        }

        //通过id监听状态, 对于周期性任务会无法收到 SUCCEEDED或者FAILED的通知，因为这两者属于终止类通知
        val live = manager.getWorkInfoByIdLiveData(request.id)
        live.observe(this, Observer {
            if (it.state == WorkInfo.State.SUCCEEDED) {
                it.outputData.run {
                    val text = getString("data")
                    text?.run { Log.i("csz", this) }
                }
            }
        })

        //通过tag监听状态
        val workInfosByTag = manager.getWorkInfosByTagLiveData("tag");
        workInfosByTag.observe(this, Observer {
            it.forEach {
                if (it.state == WorkInfo.State.SUCCEEDED) {
                    it.outputData.run {
                        val text = getString("data")
                        text?.run { Log.i("csz", this) }
                    }
                }
            }
        })

        //取消任务执行
        manager.cancelAllWorkByTag("tag")
    }

    class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

        override fun doWork(): Result {
            val text = inputData.getString("key")
            Log.i("csz", text)
            Thread.sleep(2000)
            return Result.success(workDataOf("data" to "data"))
        }

    }
}