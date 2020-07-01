package com.hydbest.baseandroid.activity.concurrent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hydbest.baseandroid.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ThreadConcurrentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concurrent);
    }

    public void _notify(View view){
        final Object obj= new Object();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("csz","thread111   start!");
                synchronized (obj) {
                    try {
                        //会释放掉当前的obj锁
                        obj.wait(5000);
                        Log.i("csz","thread111   11111111");
                    } catch (InterruptedException e) {
                        Log.i("csz","thread111   interrupted");
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
                    Log.i("csz","thread222   11111111");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i("csz","thread222   22222222");
                }
            }
        }).start();

        /*
        12-05 18:34:54.592 4752-4925/com.hydbest.baseandroid I/csz: thread111   start!
        12-05 18:35:04.591 4752-4927/com.hydbest.baseandroid I/csz: thread222   get lock
        12-05 18:35:04.591 4752-4927/com.hydbest.baseandroid I/csz: thread222   11111111
        12-05 18:35:05.592 4752-4927/com.hydbest.baseandroid I/csz: thread222   22222222
        12-05 18:35:05.593 4752-4925/com.hydbest.baseandroid I/csz: thread111   11111111
        12-05 18:35:05.593 4752-4925/com.hydbest.baseandroid I/csz: thread111   end!
        */
    }
}
