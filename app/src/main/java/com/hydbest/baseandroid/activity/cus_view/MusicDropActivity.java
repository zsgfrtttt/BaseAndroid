package com.hydbest.baseandroid.activity.cus_view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.view.view.BrulBackgroundRelativeLayout;

public class MusicDropActivity extends AppCompatActivity {
    private BrulBackgroundRelativeLayout brulBackgroundRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_drop);

        brulBackgroundRelativeLayout = findViewById(R.id.root);
    }

    public void transform(View view) {
        brulBackgroundRelativeLayout.setForeground(getDrawable(R.drawable.a));
    }
}
