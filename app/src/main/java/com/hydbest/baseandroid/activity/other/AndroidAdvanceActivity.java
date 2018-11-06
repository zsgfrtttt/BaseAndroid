package com.hydbest.baseandroid.activity.other;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.adapter.ExpandableItemAdapter;
import com.hydbest.baseandroid.entity.Fragmentation;
import com.hydbest.baseandroid.util.DataGeneration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by csz on 2018/11/6.
 */

public class AndroidAdvanceActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;

    ExpandableItemAdapter adapter;
    List<MultiItemEntity> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_advance);
        ButterKnife.bind(this);

        list = DataGeneration.getSourceItems();
        adapter = new ExpandableItemAdapter(list);

        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(adapter);

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (AndroidAdvanceActivity.this.adapter.getItemViewType(position) == ExpandableItemAdapter.TYPE_LEVEL_1){
                    final Fragmentation fragmentation = (Fragmentation) adapter.getItem(position);
                    Uri uri = Uri.parse(fragmentation.content.split("-")[1]);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }
}
