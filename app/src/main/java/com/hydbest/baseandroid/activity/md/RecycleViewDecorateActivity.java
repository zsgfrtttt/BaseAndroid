package com.hydbest.baseandroid.activity.md;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.hydbest.baseandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by csz on 2019/6/10.
 */

public class RecycleViewDecorateActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_decorate);
        ButterKnife.bind(this);
    }
}
