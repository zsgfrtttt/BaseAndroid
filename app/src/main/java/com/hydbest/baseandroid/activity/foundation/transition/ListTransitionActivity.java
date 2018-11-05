package com.hydbest.baseandroid.activity.foundation.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.adapter.TextAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by csz on 2018/11/5.
 */

public class ListTransitionActivity extends AppCompatActivity {
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.iv)
    ImageView mIv;
    @BindView(R.id.root)
    LinearLayout mRoot;

    TextAdapter mTextAdapter;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transition);
        ButterKnife.bind(this);

        mTextAdapter = new TextAdapter(null);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        mTextAdapter.bindToRecyclerView(mRv);
        initEvent();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initEvent() {
        mTextAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                start(adapter.getViewByPosition(position, R.id.iv));
            }
        });
        mIv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                start(v);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void start(View view) {
        Intent intent = new Intent(this, DetailTransitionActivity.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "userAvatar");
        startActivity(intent, optionsCompat.toBundle());
    }

    /**
     * 扩散效果
     *
     * @param view
     */
    public void createCircularReveal(View view) {
        int cx = mRoot.getWidth() / 2;
        int cy = mRoot.getHeight() / 2;
        float finalRadius = (float) Math.hypot(cx, cy);
        Animator anim = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(mRoot, cx, cy, 0f, finalRadius).setDuration(5000);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            });
            anim.start();
        }

    }
}
