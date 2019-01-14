package com.hydbest.baseandroid.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.csz.video.media.SlotValue;
import com.csz.video.media.VideoLayout;
import com.hydbest.baseandroid.R;

import java.util.List;

public class VideoAdapter extends BaseQuickAdapter<SlotValue,BaseViewHolder>{


    public VideoAdapter(@Nullable List<SlotValue> data) {
        super(R.layout.item_video,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SlotValue item) {
        LinearLayout parent = helper.getView(R.id.root);
        if (parent.getChildCount()==0) {
            VideoLayout layout = new VideoLayout(parent.getContext(),parent);
            layout.setDataSource(item.getUrl());
            parent.addView(layout);
        }

    }
}
