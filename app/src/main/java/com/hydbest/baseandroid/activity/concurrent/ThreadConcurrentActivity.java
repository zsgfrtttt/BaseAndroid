package com.hydbest.baseandroid.activity.concurrent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hydbest.baseandroid.R;

public class ThreadConcurrentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concurrent);
    }

    public void notify(View view) throws InterruptedException {
        final Object obj= new Object();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("csz","thread111   start!");
                synchronized (obj) {
                    try {
                        //会释放掉当前的obj锁
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i("csz", "thread111   end!");
                }

            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (obj){
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (obj) {
                    Log.i("csz","thread222   get lock");
                    obj.notify();
                }
            }
        }).start();
    }
}
