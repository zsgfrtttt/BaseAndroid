package com.hydbest.baseandroid.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.adapter.ExpandableItemAdapter;
import com.hydbest.baseandroid.entity.Level0Item;
import com.hydbest.baseandroid.util.DataGeneration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
                return adapter.getItemViewType(position) == ExpandableItemAdapter.TYPE_LEVEL_0 ? manager.getSpanCount() : 1;
            }
        });

        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);
        //adapter.expandAll();
    }

}
