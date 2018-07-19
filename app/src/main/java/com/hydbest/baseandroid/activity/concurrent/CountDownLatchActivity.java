package com.hydbest.baseandroid.activity.concurrent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hydbest.baseandroid.R;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchActivity extends AppCompatActivity {
    private CountDownLatch mCountDownLatch = new CountDownLatch(2);
    private Thread thread1;
    private Thread thread2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        execTask();
    }

    private void execTask() {
        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("csz","thread1 run.");
                    Thread.sleep(10000);
                    mCountDownLatch.countDown();
                    Log.i("csz","thread1 end.");
                } catch (InterruptedException e) {
                    mCountDownLatch.countDown();
                    Log.i("csz","thread1 interrupt.");
                    e.printStackTrace();
                }
            }
        });

        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("csz","thread2 run.");
                    Thread.sleep(10000);
                    mCountDownLatch.countDown();
                    Log.i("csz","thread2 end.");
                } catch (InterruptedException e) {

                    Log.i("csz","thread2 interrupt.");
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();
    }

    public void exec(View view){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    mCountDownLatch.await();
                    Log.i("csz","thread run.");
                    Thread.sleep(10000);
                    Log.i("csz","thread end.");
                } catch (InterruptedException e) {
                    Log.i("csz","thread interrupt."+Thread.currentThread().getName());
                    e.printStackTrace();
                }
            }
        });
        thread.start();
       // thread.interrupt();
    }
}
