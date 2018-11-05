package com.hydbest.baseandroid.activity.foundation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.activity.md.MD_ButtonActivity;
import com.hydbest.baseandroid.adapter.TextAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by csz on 2018/11/5.
 */

public class ContextMenuActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.tv)
    TextView mTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_menu);
        ButterKnife.bind(this);
        registerForContextMenu(mRv);
        registerForContextMenu(mTv);

        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(new TextAdapter(null));
    }

    /**
     * @param menuInfo
     * ViewGroup.showContextMenuForChild(View originalView)
     * View.getContextMenuInfo()
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("title");
        menu.add("sub1").setIntent(new Intent(this,MD_ButtonActivity.class));
        menu.add("sub2").setIntent(new Intent(this,MD_ButtonActivity.class));
        menu.add("sub3").setIntent(new Intent(this,MD_ButtonActivity.class));
    }
}
