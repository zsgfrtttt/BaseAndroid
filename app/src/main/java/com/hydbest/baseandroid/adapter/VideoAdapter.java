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

    private Map<Integer, VideoHandler> mHandleMap = new HashMap<>();
    private int mPlayIndex = NO_POSITION;

    public VideoAdapter(@Nullable List<SlotValue> data) {
        super(R.layout.item_video, data);
        // initScrollListener();
        initRecycleListener();
    }

    @Override
    protected void convert(BaseViewHolder helper, SlotValue item) {
        int position = helper.getAdapterPosition();
        LinearLayout parent = helper.getView(R.id.root);
        VideoLayout videoLayout = helper.getView(R.id.video_layout);
        if (videoLayout == null) {
            VideoHandler handle = VideoHandler.handle(item, parent);
        }else{
            videoLayout.getVideoHandler().playWithData(item);
        }
    }

    private void initScrollListener() {
        final RecyclerView recyclerView = getRecyclerView();
        if (recyclerView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        onRvScroll();
                    }
                });
            } else {
                recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        onRvScroll();
                    }
                });
            }
        }
    }

    private void initRecycleListener() {
        final RecyclerView recyclerView = getRecyclerView();
        if (recyclerView != null) {
            recyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
                @Override
                public void onViewRecycled(RecyclerView.ViewHolder holder) {
                    VideoHandler currentHandler = VideoManager.getInstance().getCurrentHandler();
                    if (currentHandler == null) {
                        return ;
                    }
                    VideoLayout videoLayout = ((BaseViewHolder) holder).getView(R.id.video_layout);
                    if (currentHandler.getVedioLayout() != null && currentHandler.getVedioLayout() == videoLayout) {
                        VideoManager.getInstance().releaseHandler();
                    }
                }
            });
        }
    }

    private void onRvScroll() {
        RecyclerView.LayoutManager mLayoutManager = getRecyclerView().getLayoutManager();
        if (mLayoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mLayoutManager;
            int firstCompletelyVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if (firstCompletelyVisibleItemPosition != NO_POSITION) {
                VideoHandler handler = mHandleMap.get(firstCompletelyVisibleItemPosition);
                if (handler != null) {
                    handler.resumeByFilter();
                    VideoHandler oldHandler = mHandleMap.get(mPlayIndex);
                    if (oldHandler != null) {
                        oldHandler.pause();
                    }
                }
            }
            mPlayIndex = firstCompletelyVisibleItemPosition;
        }

    }

}
