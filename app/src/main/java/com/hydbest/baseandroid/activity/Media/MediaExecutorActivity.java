package com.hydbest.baseandroid.activity.Media;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hydbest.baseandroid.R;

import butterknife.ButterKnife;

/**
 * Created by csz on 2018/9/14.
 */

public class MediaExecutorActivity extends AppCompatActivity{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_executor);
        ButterKnife.bind(this);
    }
}
