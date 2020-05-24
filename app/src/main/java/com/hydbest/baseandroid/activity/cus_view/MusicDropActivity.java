package com.hydbest.baseandroid.activity.cus_view;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
