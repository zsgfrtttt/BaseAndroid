package com.hydbest.baseandroid.activity.other;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.adapter.ExpandableItemAdapter;
import com.hydbest.baseandroid.entity.Fragmentation;
import com.hydbest.baseandroid.util.DataGeneration;

import java.lang.reflect.Method;
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

    List<BaseNode> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_advance);
        ButterKnife.bind(this);

        list = DataGeneration.getSourceItems();
        adapter = new ExpandableItemAdapter(list);

        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(adapter);

        adapter.setOnItemLongClickListener(new com.chad.library.adapter.base.listener.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (AndroidAdvanceActivity.this.adapter.getItemViewType(position) == ExpandableItemAdapter.TYPE_LEVEL_1) {
                    final Fragmentation fragmentation = (Fragmentation) adapter.getItem(position);
                    Uri uri = Uri.parse(fragmentation.content.split("-")[1]);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    Test test = new Test();
                    test.main();
                    return true;
                }
                return false;
            }
        });
    }

    public static class Test {

        public static class A {
            private void eat() {
                Log.i("csz", "eeeeeeeeeeeeeee   " + this.getClass().getName());
            }
        }

        public static class B extends A {

        }

        public static class C {

            private void eat() {
                Log.i("csz", "cccccccc   " + this.getClass().getName());
            }

        }

        public void main() {
            try {
                Method eat = A.class.getDeclaredMethod("eat");
                eat.setAccessible(true);
                B instance = B.class.newInstance();
                eat.invoke(instance);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("csz", "!!!!!!!   " + e.getMessage());
            }

        }
    }
}
