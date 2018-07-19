package com.hydbest.baseandroid.activity.concurrent;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hydbest.baseandroid.R;

import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);
        ((TextView)findViewById(R.id.btn)).setText(Build.MANUFACTURER);
    }

    //已经知道结果
    public void futuretask(View view) {
        FutureTask<Integer> task = new FutureTask<Integer>(new TimerTask() {
            @Override
            public void run() {
                try {
                    Log.i("csz", "th start");
                    Thread.sleep(3000);
                    Log.i("csz", "th stop");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 10);

        new Thread(task).start();

        try {
            Integer target = task.get();
            Log.i("csz", "result:" + target);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //未知道结果
    public void futuretask1(View view) {
        final FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Log.i("csz", "th   start");
                Thread.sleep(3000);
                Log.i("csz", "th end");
                return 100 + 100;
            }
        });

        new Thread(task).start();

        final Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    Integer target = task.get(4, TimeUnit.SECONDS);
                    Log.i("csz", "result:" + target);
                } catch (InterruptedException e) {
                    Log.i("csz","interupt");
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        };
        th.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                th.interrupt();
            }
        }).start();
    }
}
