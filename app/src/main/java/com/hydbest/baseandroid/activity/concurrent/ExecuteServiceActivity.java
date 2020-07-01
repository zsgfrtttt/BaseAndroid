package com.hydbest.baseandroid.activity.concurrent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * shutdownNow会抛出interupt异常
 * isShutDown 在shutdown后就会更改标志位
 * isTerminater 在任务执行完毕后才会更改标志位
 */

public class ExecuteServiceActivity extends AppCompatActivity {
    private ExecutorService mThreadPool;

    private volatile boolean mRunService = true;

    private Thread mThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.hydbest.baseandroid.R.layout.activity_execute_service);
        initService();
    }

    private void initService() {
        int count = Runtime.getRuntime().availableProcessors();
        Log.i("csz", "count:" + count);
        mThreadPool = Executors.newFixedThreadPool(count);

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                while (!mThreadPool.isShutdown()) {
                    try {
                        mThread = Thread.currentThread();
                        Thread.sleep(5000);
                        Log.i("csz", "isRunning!");
                    } catch (InterruptedException e) {
                        Log.i("csz", "interupt.");
                        e.printStackTrace();
                    }
                }
            }

        });

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    boolean shutdown = mThreadPool.isShutdown();
                    boolean terminated = mThreadPool.isTerminated();
                    Log.i("csz", "shutdown:" + shutdown + "    terminated:" + terminated);
                    if (shutdown && terminated) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void shutdown(View view) {
        mThreadPool.shutdownNow();
    }

    public void terminated(View view) {
        //mThreadPool
    }

    public void interupt(View view) {
        if (mThread != null) {
            mThread.interrupt();
        }
    }
}
