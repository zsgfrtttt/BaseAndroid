package com.csz.video.media;

import android.content.Context;
import android.media.AudioManager;
import android.text.TextUtils;
import android.view.ViewGroup;

public class VideoHandler implements VideoLayout.VidioPlayerListener {

    private static final int MILLION_SECOND = 1000;

    private Context mContext;
    private VideoLayout mVedioLayout;
    private ViewGroup mParentLayout;

    private SlotValue mSlotValue;
    private SlotCallback mSlotCallback;

    private boolean mAutoPause;//防止滑动时多次调用
    private int lastArea = 0;

    private VideoHandler(SlotValue data,ViewGroup parentLayout) {
        this.mSlotValue = data;
        this.mParentLayout = parentLayout;
        this.mContext = parentLayout.getContext();
        initVedioView();
    }

    public static VideoHandler handle(SlotValue data,ViewGroup parentLayout){
        return new VideoHandler(data,parentLayout);
    }

    private void initVedioView() {
        mVedioLayout = new VideoLayout(mContext, mParentLayout);
        if (mSlotValue != null) {
            mVedioLayout.setDataSource(mSlotValue.getUrl());
            mVedioLayout.setVidioPlayerListener(this);
        }
        mParentLayout.addView(mVedioLayout);
    }

    public void onParentScroll(){
        int curArea = Util.getVisiblePercent(mParentLayout);
        if (curArea <=0){
            return;
        }else if (curArea <= 50){
            if (mAutoPause){
                mVedioLayout.pause();
                mAutoPause = false;
            }
            lastArea = 0;
            mVedioLayout.setIsComplete(false);
            mVedioLayout.setIsPauseClick(false);
        }else if (mVedioLayout.isComplete() || mVedioLayout.isPauseBtnClicked()){
            if (mAutoPause){
                mVedioLayout.pause();
                mAutoPause = false;
            }
        }

        if (Util.canAutoPlay(mContext)){
            mVedioLayout.resume();
            mAutoPause =true;
            lastArea = curArea;
            mVedioLayout.setIsPauseClick(false);
        }else {
            mVedioLayout.pause();
            mVedioLayout.setIsPauseClick(true);
        }

    }


    @Override
    public void onBufferUpdate(int time) {

    }

    @Override
    public void onClickFullScreen() {
        mParentLayout.removeView(mVedioLayout);
        VideoFullDialog dialog = new VideoFullDialog(mContext,mVedioLayout,mSlotValue);
        dialog.setListener(new VideoFullDialog.FullToSmallListener() {
            @Override
            public void onFullToSmall(int position) {
                backToSmall(position);
            }

            @Override
            public void onPlayComplete() {

            }
        });
        dialog.show();
    }

    private void backToSmall(int position) {
        if (mVedioLayout.getParent() == null){
            mParentLayout.addView(mVedioLayout);

            mVedioLayout.showFullBtn(true);
            mVedioLayout.setVidioPlayerListener(this);
            mVedioLayout.seekAndResume(position);
        }
    }

    @Override
    public void onClickVideo() {
        if (mSlotValue != null && TextUtils.isEmpty(mSlotValue.getClickUrl())) {
            //进入web广告页
        }
    }

    @Override
    public void onVideoLoadSuccess() {
        if (mSlotCallback != null) {
            mSlotCallback.onVideoLoadSuccess();
        }
    }

    @Override
    public void onVideoPlayComplete() {
        if (mSlotCallback != null) {
            mSlotCallback.onVideoPlayComplete();
        }
    }

    @Override
    public void onVideoLoadFail() {
        mAutoPause = false;
        if (mSlotCallback != null) {
            mSlotCallback.onVideoLoadFail();
        }
    }

    public int getPosition() {
        return mVedioLayout.getCurrentPosition() / MILLION_SECOND;
    }

    public int getDuration() {
        return mVedioLayout.getDuration() / MILLION_SECOND;
    }
}
