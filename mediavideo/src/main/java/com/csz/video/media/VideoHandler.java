package com.csz.video.media;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.csz.video.R;

public class VideoHandler implements VideoLayout.VidioPlayerListener, IVideoOperation {

    private static final int MILLION_SECOND = 1000;
    private static int FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
    private static int NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    private Context mContext;
    private VideoLayout mVedioLayout;
    private ViewGroup mParentLayout;

    private SlotValue mSlotValue;
    private SlotCallback mSlotCallback;

    private boolean mAutoPause;//防止滑动时多次调用
    private int lastArea = 0;
    private int mOritation;

    private VideoHandler(SlotValue data, ViewGroup parentLayout) {
        this.mSlotValue = data;
        this.mParentLayout = parentLayout;
        this.mContext = parentLayout.getContext();
        initVedioView();
    }

    public static VideoHandler handle(SlotValue data, ViewGroup parentLayout) {
        return new VideoHandler(data, parentLayout);
    }

    private void initVedioView() {
        int oritation = Util.scanForActivity(mContext).getRequestedOrientation();
        mVedioLayout = new VideoLayout(mContext, mParentLayout, this, mSlotValue.isAutoPlay(), oritation);
        if (mSlotValue != null) {
            mVedioLayout.setDataSource(mSlotValue.getUrl());
            mVedioLayout.setVidioPlayerListener(this);
        }
        VideoLayout child = mParentLayout.findViewById(R.id.video_layout);
        if (child != null) {
            mParentLayout.removeView(child);
        }
        mParentLayout.addView(mVedioLayout);
    }

    public void onParentScroll() {
        int curArea = Util.getVisiblePercent(mParentLayout);
        if (curArea <= 0) {
            return;
        } else if (curArea <= 50) {
            if (mAutoPause) {
                mVedioLayout.pause(mVedioLayout.isPauseBtnClicked());
                mAutoPause = false;
            }
            lastArea = 0;
            mVedioLayout.setIsComplete(false);
            mVedioLayout.setIsPauseClick(false);
        } else if (mVedioLayout.isComplete() || mVedioLayout.isPauseBtnClicked()) {
            if (mAutoPause) {
                mVedioLayout.pause(mVedioLayout.isPauseBtnClicked());
                mAutoPause = false;
            }
        }

        if (Util.canAutoPlay(mContext)) {
            if (!mVedioLayout.isPauseBtnClicked()) {
                mVedioLayout.resume();
                mVedioLayout.setIsPauseClick(false);
            }
            mAutoPause = true;
            lastArea = curArea;
        } else {
            mVedioLayout.pause(mVedioLayout.isPauseBtnClicked());
            mVedioLayout.setIsPauseClick(true);
        }

    }


    @Override
    public void onBufferUpdate(int time) {

    }

    /**
     * 切换横屏时需要在manifest的activity标签下添加android:configChanges="orientation|keyboardHidden|screenSize"配置，
     * 以避免Activity重新走生命周期
     */
    @Override
    public void onClickFullScreen() {
        Util.hideActionBar(mContext);
        Util.scanForActivity(mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (mOritation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || mOritation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
        }else{
            mVedioLayout.sendRequestOritationMsg();
        }

        ViewGroup contentView = Util.scanForActivity(mContext).findViewById(android.R.id.content);
        if (mVedioLayout.getParent() != null) {
            ((ViewGroup) mVedioLayout.getParent()).removeView(mVedioLayout);
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        contentView.addView(mVedioLayout, params);
        mVedioLayout.onFullScreen(true);
    }

    @Override
    public void onExitFullScreen() {
        Util.showActionBar(mContext);
        Util.scanForActivity(mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (mOritation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || mOritation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
        }else{
            mVedioLayout.sendRequestOritationMsg();
        }

        if (mVedioLayout.getParent() != null) {
            ((ViewGroup) mVedioLayout.getParent()).removeView(mVedioLayout);
        }
        mParentLayout.addView(mVedioLayout);
        mVedioLayout.onFullScreen(false);

    }

    private void backToSmall(int position) {
        if (mVedioLayout.getParent() == null) {
            mParentLayout.addView(mVedioLayout);
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

    public boolean isFullScreen() {
        return mVedioLayout.isFullScreen();
    }

    public VideoLayout getVedioLayout() {
        return mVedioLayout;
    }

    /**
     * 销毁所有资源,结束播放
     */
    public void release() {
        if (isFullScreen()) {
            onExitFullScreen();
        }
        // 释放播放器相关资源
        mVedioLayout.stop();
        Runtime.getRuntime().gc();
    }

    @Override
    public void resume() {
        if (mVedioLayout != null) {
            mVedioLayout.resume();
        }
    }

    public void resumeByFilter() {
        if (mVedioLayout != null) {
            mVedioLayout.resumeByFilter();
        }
    }

    @Override
    public void pause(boolean isRealPause) {
        if (mVedioLayout != null) {
            mVedioLayout.pause(isRealPause);
        }
    }

    public void pause() {
        if (mVedioLayout != null) {
            mVedioLayout.pause(mVedioLayout.isPauseBtnClicked());
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mVedioLayout != null) {
            if (isFullScreen()) {
                onExitFullScreen();
                return true;
            } else {
                release();
            }
        }
        return false;
    }

    @Override
    public void playWithData(SlotValue data) {
        if (mVedioLayout != null) {
            this.mSlotValue = data;
            mVedioLayout.setDataSource(data.getUrl());
            mVedioLayout.load();
        }
    }
}
