package com.hydbest.baseandroid.activity.event;

import android.os.Bundle;
import android.view.View;

import com.hydbest.baseandroid.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import groovy.csz.eventbus.EventBus;

public class EventPostActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_post);
    }

    public void post(View view){
        EventBus.getInstatnce().post("this is a new message");
        finish();
    }
}
