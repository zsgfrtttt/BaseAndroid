package com.hydbest.baseandroid.activity.event;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import groovy.csz.eventbus.EventBus;
import com.hydbest.baseandroid.R;

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
