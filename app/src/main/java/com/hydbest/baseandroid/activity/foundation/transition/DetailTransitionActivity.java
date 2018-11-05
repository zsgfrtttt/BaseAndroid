package com.hydbest.baseandroid.activity.foundation.transition;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.hydbest.baseandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by csz on 2018/11/5.
 */

public class DetailTransitionActivity extends AppCompatActivity {

    @BindView(R.id.iv)
    ImageView mIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_detail);
        ButterKnife.bind(this);
    }
}
