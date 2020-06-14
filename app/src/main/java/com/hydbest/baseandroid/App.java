package com.hydbest.baseandroid;

import android.app.Application;
import android.content.Context;

import com.csz.runtime.ActivityBuilder;

import androidx.multidex.MultiDex;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActivityBuilder.INSTANCE.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
