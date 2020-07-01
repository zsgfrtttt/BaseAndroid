package com.hydbest.baseandroid.adapter;

import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.csz.video.media.SlotValue;
import com.csz.video.media.VideoHandler;
import com.csz.video.media.VideoLayout;
import com.hydbest.baseandroid.R;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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
