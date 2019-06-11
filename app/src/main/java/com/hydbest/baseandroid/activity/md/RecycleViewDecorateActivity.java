package com.hydbest.baseandroid.activity.md;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.adapter.TextAdapter;
import com.hydbest.baseandroid.view.decoration.StickyDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by csz on 2019/6/10.
 */

public class RecycleViewDecorateActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;
    private TextAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_decorate);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        adapter = new TextAdapter(null);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.addItemDecoration(new StickyDecoration());
        mRv.setAdapter(adapter);
    }
}
