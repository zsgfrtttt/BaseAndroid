package com.hydbest.baseandroid.activity.jetpack

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.kt.*
import kotlinx.android.synthetic.main.activity_kt.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import kotlin.concurrent.thread
import kotlin.coroutines.*

class KtActivity : AppCompatActivity() {

    var model: MyViewModel? = null

    val media: MediatorLiveData<Integer> = MediatorLiveData()
    val str: MutableLiveData<String> = MutableLiveData<String>()


    init {
        media.addSource(str) {
            Log.i("csz", "observe " + it)
        }
    }

    val scope by lazy { MainScope() }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kt)
        //   setContentView(R.layout.activity_csz)

        //  lifecycleScope.launch { el() }
        // val model by viewModels<MyViewModel>()
        model = viewModels<MyViewModel>().value

        model?.getData()?.observe(this, object : Observer<Int> {
            override fun onChanged(t: Int?) {
                btn.setText(t.toString())
            }
        })
        media.observe(this) {
            Log.i("csz", "kk " + it)
        }


        btn.setOnClickListener {
            lifecycleScope.launch {
               val result = alert("Warn","Do you like it")
                Log.i("csz","result $result  ${Thread.currentThread().name}")
            }
        }

    }

    suspend fun Context.alert(title: String, message: String): Boolean
    = suspendCancellableCoroutine {
        con ->
        Log.i("csz","con   ${Thread.currentThread().name}")
       AlertDialog.Builder(this)
               .setNegativeButton("No"){
                   dialog, which ->
                   dialog.dismiss()
                   con.resume(false)
               }.setPositiveButton("Yes"){
                   dialog, which ->
                   dialog.dismiss()
                   con.resume(true)
               }.setTitle(title)
               .setMessage(message)
               .setOnCancelListener {
                   con.resume(false)
               }.create()
               .also {
                   dialog ->
                   con.invokeOnCancellation {
                       dialog.dismiss()
                   }
               }.show()

    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    fun CoroutineScope.fetchFromNet() = async(Dispatchers.IO) {
        delay(2000)
        Log.i("csz", "fetchFromNet  ${Thread.currentThread().name}")
        "fetchFromNet"
    }

    fun CoroutineScope.fetchFromLocal() = async(Dispatchers.IO) {
        delay(1500)
        Log.i("csz", "fetchFromLocal  ${Thread.currentThread().name}")
        "fetchFromLocal"
    }

    fun <R, T> launchCoroutine(receiver: R, block: suspend R.() -> T) {
        block.startCoroutine(receiver, object : Continuation<T> {
            override val context: CoroutineContext
                get() = LogInterceptor()

            override fun resumeWith(result: Result<T>) {
                Log.i("csz", "resumeWith   ${Thread.currentThread().name}  ${System.currentTimeMillis()}  ${result.getOrNull()}")
            }

        })
    }

    inner class Scope<T> {
        fun receive(va: T, p: (T) -> Unit) {
            p(va)
        }
    }

    inner class LogInterceptor : ContinuationInterceptor {
        override val key: CoroutineContext.Key<*> = ContinuationInterceptor

        override fun <T> interceptContinuation(continuation: Continuation<T>) = LogContinuation(continuation)
    }

    inner class LogContinuation<T>(private val continuation: Continuation<T>) : Continuation<T> by continuation {
        override fun resumeWith(result: Result<T>) { //启动的时候也会触发
            Log.i("csz", "start ${result.isSuccess}")
            continuation.resumeWith(result)
            Log.i("csz", "end ")
        }
    }


    fun add(view: View) {
        // model?.add()
        //  delay()
        //  apply()
        str.postValue("lkllll")

    }

    suspend fun el() {
        withContext(Dispatchers.IO) {
            delay(5000)
            MainScope().launch {
                // Toast.makeText()
                Toast.makeText(this@KtActivity, "hahah ", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * 1.线程
     */
    private fun thread() {
        thread {
            Thread.sleep(5000)
            Log.i("csz", "csz end")
        }
        Log.i("csz", "csz start")
    }

    /**
     * 2.阻塞
     * runBlocking启动的协程任务会阻断当前线程，直到该协程执行结束
     */
    private fun delay() = runBlocking {
        repeat(8) {
            Log.e("csz", "协程执行$it 线程id：${Thread.currentThread().id}")
            delay(1000)
        }
        Log.i("csz", "csz start")
    }

    /**
     * 2.协程
     * Dispatchers.Main 主线程
     * Dispatchers.IO 外部线程
     * Dispatchers.Default 外部线程
     * Dispatchers.Unconfined 调用者的线程
     */
    private fun launch() {
        val job = GlobalScope.launch {
            delay(6000)
            Log.e("csz", "协程执行结束 -- 线程id：${Thread.currentThread().id}")
        }
        Log.e("csz", "主线程执行结束")
    }

    /**
     * 2.协程挂起
     */
    private fun suspend() {
        GlobalScope.launch {
            val result1 = async {
                getResult1()
            }
            val result2 = async {
                getResult2()
            }
            Log.e("csz", "start")
            val result = result1.await() + result2.await()
            Log.e("csz", "result = $result  ${result1.await()}   ${result2.await()}")
        }
    }

    /**
     * 2.协程运用
     */
    private fun apply() {
        GlobalScope.launch(Dispatchers.Main) {
            launch(Dispatchers.IO) {
                Log.i("csz", "launch ${Thread.currentThread().name}")
            }
            val result1 = async { getResult1() }
            val result2 = async { getResult2() }
            suspend { Log.i("csz", "suspend ${Thread.currentThread().name}") }
            val target = getResult(result1, result2)
            Log.i("csz", "end ${target}  ${Thread.currentThread().name}")
        }
    }

    private suspend fun getResult1(): Int {
        delay(3000)
        return 1
    }

    private suspend fun getResult2(): Int {
        delay(4000)
        return 2
    }

    private suspend fun getResult(result1: Deferred<Int>, result2: Deferred<Int>): Int {
        return result1.await() + result2.await()
    }

    class MyViewModel(val handle: SavedStateHandle) : ViewModel() {
        val key = "key"
        fun add() {
            handle.getLiveData(key, 0).value = handle.getLiveData(key, 0).value!!.plus(1)
            Log.i("csz", "v " + handle.getLiveData(key, 0).value)
        }

        fun getData(): LiveData<Int> {
            return handle.getLiveData(key, 0)
        }
    }

    interface Computer

    class C1 : Computer
    class C2 : Computer

    interface Factory {
        fun create(): Computer

        companion object {
            inline operator fun <reified T : Computer> invoke(): Computer =
                    when (T::class) {
                        C1::class ->
                            C1()
                        C2::class ->
                            C2()
                        else -> throw IllegalArgumentException()
                    }
        }
    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

        }


    }


}