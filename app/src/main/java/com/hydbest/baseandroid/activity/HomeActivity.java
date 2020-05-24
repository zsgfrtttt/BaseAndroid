package com.hydbest.baseandroid.activity;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.node.BaseNode;
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
    List<BaseNode> list;

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
                if (adapter.getItemViewType(position) == BaseQuickAdapter.LOAD_MORE_VIEW){
                    return manager.getSpanCount();
                }
                return adapter.getItemViewType(position) == ExpandableItemAdapter.TYPE_LEVEL_0 ? manager.getSpanCount() : 1;
            }
        });
        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);
        //adapter.expandAll();
    }

}
