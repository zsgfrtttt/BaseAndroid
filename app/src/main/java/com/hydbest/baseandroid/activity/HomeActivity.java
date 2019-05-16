package com.hydbest.baseandroid.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.adapter.ExpandableItemAdapter;
import com.hydbest.baseandroid.util.DataGeneration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.rv)
    RecyclerView rv;

    ExpandableItemAdapter adapter;
    List<MultiItemEntity> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initView();

       // startActivity();
    }

    private void initView() {
        list = DataGeneration.getItems();
        adapter = new ExpandableItemAdapter(list);

        final GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) == BaseQuickAdapter.LOADING_VIEW){
                    return manager.getSpanCount();
                }
                return adapter.getItemViewType(position) == ExpandableItemAdapter.TYPE_LEVEL_0 ? manager.getSpanCount() : 1;
            }
        });
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.loadMoreEnd();
                    }
                },3000);
            }
        },rv);

        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);
        //adapter.expandAll();
    }

}
