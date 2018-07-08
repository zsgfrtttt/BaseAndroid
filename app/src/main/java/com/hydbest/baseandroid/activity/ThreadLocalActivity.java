package com.hydbest.baseandroid.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hydbest.baseandroid.R;

import java.util.concurrent.Semaphore;

public class ThreadLocalActivity extends AppCompatActivity {

    ThreadLocal<String> mLocal;

    private HandlerThread mTh1, mTh2;
    private Handler mH1, mH2;

    private Semaphore mSemaphore = new Semaphore(1);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_local);
        initLocal();
    }

    private void initLocal() {

        mLocal = new ThreadLocal<String>() {
            @Override
            protected String initialValue() {
                //设置初始值  否则会为null　
                return "main";
            }
        };

        mTh1 = new HandlerThread("1");
        mTh2 = new HandlerThread("2");
        mTh2.start();
        mTh1.start();

        mH1 = new Handler(mTh1.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.i("csz", "h:" + Thread.currentThread().getName());
                if (msg.what == 0) {
                    String s = mLocal.get();
                    Log.i("csz", "str1:" + s);
                } else if (msg.what == 1) {
                    mLocal.set("th111");
                }
            }
        };
        mH2 = new Handler(mTh2.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.i("csz", "h:" + Thread.currentThread().getName());
                if (msg.what == 0) {
                    String s = mLocal.get();
                    Log.i("csz", "str2:" + s);
                } else if (msg.what == 1) {
                    mLocal.set("th2222");
                }
            }
        };
    }

    public void setLocal1(View view) {
        mH1.sendEmptyMessage(1);
    }

    public void setLocal2(View view) {
        mH2.sendEmptyMessage(1);
    }

    public void getLocal1(View view) {
        mH1.sendEmptyMessage(0);
    }

    public void getLocal2(View view) {
        mH2.sendEmptyMessage(0);
    }

    public void getLocalMain(View view) {
        Log.i("csz", mLocal.get() + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTh1.quit();
        mTh2.quit();
    }
}
