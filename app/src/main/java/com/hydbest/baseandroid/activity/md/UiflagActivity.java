package com.hydbest.baseandroid.activity.md;

import android.os.Bundle;
import android.view.View;

import com.hydbest.baseandroid.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UiflagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_flag);
        View viewById = findViewById(R.id.root);
        viewById.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
       // viewById.setFitsSystemWindows();
    }
}
