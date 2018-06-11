package com.hydbest.baseandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hydbest.baseandroid.view.SimpleTextureView;

public class MainActivity extends AppCompatActivity {
    private SimpleTextureView mSimpleTextureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSimpleTextureView = findViewById(R.id.simple);
    }

    public void start(View view) {
        mSimpleTextureView.startVideo();
    }
}
