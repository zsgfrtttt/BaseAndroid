package com.hydbest.baseandroid.activity.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import groovy.csz.eventbus.EventBus;
import com.hydbest.baseandroid.R;

public class EventRegisterActivity extends AppCompatActivity {
    private TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register);
        tv = findViewById(R.id.tv);
    }

    public void startPost(View view){
        startActivity(new Intent(this,EventPostActivity.class));
    }

    public void register(View view) {
        EventBus.getInstatnce().register(this);
    }

    public void unregister(View view) {
        EventBus.getInstatnce().unregister(this);
    }

    private void onEventMain(String text) {
        tv.setText(text);
    }
}
