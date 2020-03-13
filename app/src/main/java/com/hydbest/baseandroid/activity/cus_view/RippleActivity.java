package com.hydbest.baseandroid.activity.cus_view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.view.view.RippleView;

public class RippleActivity extends AppCompatActivity {

    private RippleView rippleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple);

        rippleView = findViewById(R.id.ripple);
    }

    public void click(View view) {
        if (rippleView.isRunning()) {
            rippleView.stopAnim();
        }else {
            rippleView.startAnim();
        }
    }
}
