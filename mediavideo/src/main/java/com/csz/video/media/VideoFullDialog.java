package com.csz.video.media;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.csz.video.R;
import com.csz.video.media.VideoLayout.VidioPlayerListener;

public class VideoFullDialog extends Dialog implements VidioPlayerListener {

    private VideoLayout mVideoLayout;
    private ViewGroup mParent;
    private ImageView mIvBack;

    private SlotValue mSlotValue;
    private FullToSmallListener mFullToSmallListener;
    private int mPosition ;
    private boolean isFrist;

    public VideoFullDialog(@NonNull Context context,VideoLayout videoLayout,SlotValue value) {
        super(context);
        mSlotValue = value;
        mVideoLayout = videoLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_video);
        initVideoLayout();
    }

    private void initVideoLayout() {
        mParent = findViewById(R.id.root);
        mIvBack = findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBack();
            }
        });
        mVideoLayout.setVidioPlayerListener(this);
        mParent.addView(mVideoLayout);
    }

    private void clickBack() {
        dismiss();
        if (mFullToSmallListener!= null){
            mFullToSmallListener.onFullToSmall(mVideoLayout.getCurrentPosition());
        }
    }

    @Override
    public void onBackPressed() {
        clickBack();
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus){
            mPosition = mVideoLayout.getCurrentPosition();
            mVideoLayout.pause(mVideoLayout.isPauseBtnClicked());
        }else {
            if (isFrist){
                isFrist = false;
                mVideoLayout.seekAndResume(mPosition);
            }else {
                mVideoLayout.resume();
            }
        }
    }

    @Override
    public void dismiss() {
        mParent.removeView(mVideoLayout);
        super.dismiss();
    }

    @Override
    public void onBufferUpdate(int time) {

    }

    @Override
    public void onClickFullScreen() {

    }

    @Override
    public void onExitFullScreen() {

    }

    @Override
    public void onClickVideo() {

    }

    @Override
    public void onVideoLoadSuccess() {

    }

    @Override
    public void onVideoPlayComplete() {
        if (mFullToSmallListener!= null){
            mFullToSmallListener.onPlayComplete();
        }
    }

    @Override
    public void onVideoLoadFail() {

    }

    public void setListener(FullToSmallListener listener){
        this.mFullToSmallListener = listener;
    }

    public interface FullToSmallListener {
        void onFullToSmall(int position);

        void onPlayComplete();
    }
}
