package com.hydbest.baseandroid;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.csz.okhttp.Downloader;
import com.csz.okhttp.http.DownloadCallback;
import com.csz.okhttp.http.DownloadManager;

import java.io.File;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

public class App extends Application {
    private boolean init;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override//187.2243  格隆汇  156.3136
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new AppObserver());
    }

    private static class AppObserver implements LifecycleObserver{

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        public void onCreate(){
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void onStart(){
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void onResume(){
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onPause(){
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void onStop(){
        }
    }
}
