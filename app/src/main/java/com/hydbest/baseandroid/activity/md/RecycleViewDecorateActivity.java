package com.hydbest.baseandroid.activity.md;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
