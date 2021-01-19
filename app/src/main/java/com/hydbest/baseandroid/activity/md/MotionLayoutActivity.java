package com.hydbest.baseandroid.activity.md;

import android.os.Bundle;
import android.os.Handler;

import com.hydbest.baseandroid.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class MotionLayoutActivity extends AppCompatActivity {

    private MotionLayout motion_container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_layout);

        motion_container = findViewById(R.id.layout);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                motion_container.transitionToEnd();
            }
        }, 2000);

    }
}
