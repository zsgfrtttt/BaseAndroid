package com.hydbest.baseandroid.activity.Media;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.csz.video.media.SlotValue;
import com.csz.video.media.VideoHandler;
import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.adapter.VideoAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaCusVideoActivity extends AppCompatActivity {
    @BindView(R.id.lyout_top)
    RelativeLayout relativeLayout;
    @BindView(R.id.rv)
    protected RecyclerView mRecycleView;

    private List<SlotValue> mData = new ArrayList<>();
    private VideoHandler handle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_video);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        for (int i = 0; i < 10; i++) {
            mData.add(new SlotValue("https://vd.yinyuetai.com/hd.yinyuetai.com/uploads/videos/common/90B8015D26C51713A86A1B985458D61E.mp4", null,i==0));
        }
        VideoAdapter adapter = new VideoAdapter(mData);

        final LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(manager);
        adapter.bindToRecyclerView(mRecycleView);

        //handle = VideoHandler.handle(new SlotValue("https://vd.yinyuetai.com/hd.yinyuetai.com/uploads/videos/common/90B8015D26C51713A86A1B985458D61E.mp4", null), relativeLayout);
    }

    @Override
    public void onBackPressed() {
        if (handle == null) {
            super.onBackPressed();
            return;
        }
        if (handle.onBackPressed()) return;
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        if (handle != null) {
            handle.pause();
        }
        super.onPause();
    }
}
