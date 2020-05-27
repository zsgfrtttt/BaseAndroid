package com.hydbest.baseandroid;

import android.app.Application;

import com.csz.runtime.ActivityBuilder;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActivityBuilder.INSTANCE.init(this);
    }
}
