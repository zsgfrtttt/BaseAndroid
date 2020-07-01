package com.hydbest.baseandroid.activity.foundation;

import android.os.Bundle;

import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.hydbest.baseandroid.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class VlayoutActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vlayout);

        recyclerView = findViewById(R.id.rv);
        VirtualLayoutManager manager = new VirtualLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        pool.setMaxRecycledViews(0,10);
        recyclerView.setRecycledViewPool(pool);

        // BaseViewHolder
    }
}
