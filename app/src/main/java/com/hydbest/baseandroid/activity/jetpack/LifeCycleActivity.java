package com.hydbest.baseandroid.activity.jetpack;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.Chronometer;

import com.hydbest.baseandroid.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class LifeCycleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle);
        Chrom ch = findViewById(R.id.ch);
        getLifecycle().addObserver(ch);
    }

    public static class Chrom extends Chronometer implements LifecycleObserver {
        //now - base
        private long time ;

        public Chrom(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        private void onPause(){
            time = SystemClock.elapsedRealtime() - getBase();
            stop();
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        private void onResume(){
            setBase(SystemClock.elapsedRealtime() - time);
            start();
        }

    }
}
