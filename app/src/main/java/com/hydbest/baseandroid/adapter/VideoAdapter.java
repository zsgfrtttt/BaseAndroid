package com.hydbest.baseandroid.adapter;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.csz.video.media.SlotValue;
import com.csz.video.media.VideoHandler;
import com.csz.video.media.VideoLayout;
import com.csz.video.media.VideoManager;
import com.hydbest.baseandroid.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

public class VideoAdapter extends BaseQuickAdapter<SlotValue, BaseViewHolder> {

    public VideoAdapter(@Nullable List<SlotValue> data) {
        super(R.layout.item_video, data);
        initRecycleListener();
    }

    @Override
    protected void convert(BaseViewHolder helper, SlotValue item) {
        LinearLayout parent = helper.getView(R.id.root);
        VideoHandler handle = VideoHandler.handle(item, parent);
    }

    private void initRecycleListener() {
        final RecyclerView recyclerView = getRecyclerView();
        if (recyclerView != null) {
            recyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
                @Override
                public void onViewRecycled(RecyclerView.ViewHolder holder) {
                    VideoLayout videoLayout = ((BaseViewHolder) holder).getView(R.id.video_layout);
                    if (videoLayout != null) {
                        videoLayout.getVideoHandler().release();
                    }
                }
            });
        }
    }

}
