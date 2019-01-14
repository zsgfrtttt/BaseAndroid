package com.csz.video.media;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.csz.video.R;

import java.io.IOException;

public class VideoLayout extends RelativeLayout implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, TextureView.SurfaceTextureListener {

    private static final int TIME_MSG = 0x01;
    private static final int TIME_INVAL = 1000;
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_PAUSEING = 2;
    private static final int LOAD_TOTAL_COUNT = 3;

    private ViewGroup mParentContainer;
    private RelativeLayout mPlayerView;
    private TextureView mVideoView;
    private ImageView mIvCenter;
    private ImageView mIvFull;
    private ImageView mIvSwitch;
    private LinearLayout mLayoutBottom;
    private SeekBar mSeekBar;
    private AudioManager mAudioManager;
    private Surface videoSurface;

    private String mUrl;
    private boolean isMute;
    private int mSreenWidth, mDestationHeight;

    private boolean isRealPause;
    private boolean isComplete;
    private int mCurrentCount;
    private int playState = STATE_IDLE;

    private MediaPlayer mediaPlayer;
    private VidioPlayerListener listener;
    private ScreenEventReceiver mScreenReceiver;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_MSG:
                    if (isPlaying()) {
                        //  listener.onBufferUpdate(getCurrentPosition());
                        sendEmptyMessageDelayed(TIME_MSG, TIME_INVAL);
                    }
                    break;
            }
        }
    };

    public VideoLayout(Context context, ViewGroup parentContainer) {
        super(context);
        mParentContainer = parentContainer;
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        initData();
        initView();
        registerBroadcastReceiver();
    }

    private void initData() {
        mSreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mDestationHeight = mSreenWidth * 9 / 16;
    }

    private void initView() {
        View.inflate(getContext(), R.layout.layout_video,this);
        mVideoView = findViewById(R.id.texture);
        mIvCenter = findViewById(R.id.iv_center);
        mIvFull = findViewById(R.id.iv_full);
        mIvSwitch = findViewById(R.id.iv_switch) ;
        mLayoutBottom = findViewById(R.id.layout_bottom);
        mSeekBar = findViewById(R.id.seek);

        mIvCenter.setOnClickListener(this);
        mIvFull.setOnClickListener(this);
        mIvSwitch.setOnClickListener(this);
        mVideoView.setSurfaceTextureListener(this);

        showPauseOrPlayView(false);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (listener != null) {
            listener.onVideoPlayComplete();
        }
        setIsComplete(true);
        setIsPauseClick(true);
        playBack();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (mCurrentCount >= LOAD_TOTAL_COUNT) {
            if (listener != null) {
                listener.onVideoLoadFail();
            }
            setCurrentPalyState(STATE_ERROR);
            showPauseOrPlayView(false);
        }
        stop();
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.i("csz",(Looper.getMainLooper().getThread() == Thread.currentThread())+":llll");
        mediaPlayer = mp;
        if (mediaPlayer != null) {
            mediaPlayer.setOnBufferingUpdateListener(this);
            mCurrentCount = 0;
            if (listener != null) {
                listener.onVideoLoadSuccess();
            }
            decideCanPlay();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        videoSurface = new Surface(surface);
        load();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE && playState == STATE_PAUSEING) {
            if (isRealPause || isComplete()) {
                pause();
            } else {
                decideCanPlay();
            }
        } else {
            pause();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unRegisterReceiver();
    }

    public void setDataSource(String url) {
        this.mUrl = url;
    }

    public void load() {
        if (playState != STATE_IDLE) {
            return;
        }
        try {
            showLoadingView();
            checkMediaPlayer();
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    public void resume() {
        if (playState != STATE_PAUSEING) {
            Log.i("csz","llll");
            return;
        }
        if (!isPlaying()) {
            entryResumeState();
            showPauseOrPlayView(true);
            mediaPlayer.start();
            mHandler.sendEmptyMessage(TIME_MSG);
        }
    }

    public void pause() {
        if (playState != STATE_PLAYING) {
            return;
        }
        setCurrentPalyState(STATE_PAUSEING);
        if (isPlaying()) {
            mediaPlayer.pause();
        }
        showPauseOrPlayView(false);
        mHandler.removeCallbacksAndMessages(null);
    }

    private boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    /**
     * 播放完成回到初始状态
     */
    public void playBack() {
        setCurrentPalyState(STATE_PAUSEING);
        mHandler.removeCallbacksAndMessages(null);
        if (mediaPlayer != null) {
            mediaPlayer.setOnSeekCompleteListener(null);
            mediaPlayer.seekTo(0);
            mediaPlayer.pause();
        }
        showPauseOrPlayView(false);
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.setOnSeekCompleteListener(null);
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mHandler.removeCallbacksAndMessages(null);
        setCurrentPalyState(STATE_IDLE);

        if (mCurrentCount < LOAD_TOTAL_COUNT) {
            mCurrentCount += 1;
            load();
        } else {
            showPauseOrPlayView(false);
        }
    }

    private void entryResumeState() {
        setCurrentPalyState(STATE_PLAYING);
        setIsPauseClick(false);
        setIsComplete(false);
    }

    public void destory() {

    }

    public void seekAndResume(int position) {
        setCurrentPalyState(STATE_PLAYING);
        if (mediaPlayer.isPlaying() || mediaPlayer.getCurrentPosition() > 0) {
            mediaPlayer.seekTo(position);
            decideCanPlay();
        }
    }

    public void seekAndPause(int position) {
        if (playState != STATE_PLAYING) {
            return;
        }
        setCurrentPalyState(STATE_PAUSEING);
        showPauseOrPlayView(false);
        if (isPlaying()) {
            mediaPlayer.seekTo(position);
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    mediaPlayer.pause();
                    mHandler.removeCallbacksAndMessages(null);
                }
            });
        }

    }

    public void setVidioPlayerListener(VidioPlayerListener listener) {
        this.listener = listener;
    }

    private synchronized void checkMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = createMediaPlayer();
        }
    }

    private MediaPlayer createMediaPlayer() {
        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(mUrl);
            player.setOnCompletionListener(this);
            player.setOnErrorListener(this);
            player.setOnPreparedListener(this);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            if (videoSurface != null && videoSurface.isValid()) {
                player.setSurface(videoSurface);
            } else {
                stop();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return player;
    }

    private void registerBroadcastReceiver() {
        mScreenReceiver = new ScreenEventReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        getContext().registerReceiver(mScreenReceiver, filter);
    }

    private void unRegisterReceiver() {
        if (mScreenReceiver != null) {
            getContext().unregisterReceiver(mScreenReceiver);
            mScreenReceiver = null;
        }
    }

    private void decideCanPlay() {
        if (mParentContainer.getWidth() * 2 > mSreenWidth) {
            setCurrentPalyState(STATE_PAUSEING);
            resume();
        } else {
            pause();
        }
    }

    public void showFullBtn(boolean show) {
    }

    public boolean isPauseBtnClicked() {
        return isRealPause;
    }

    public boolean isComplete() {
        return isComplete;
    }

    /**
     * @param play true:播放    false：暂停
     */
    private void showPauseOrPlayView(boolean play) {
        if (play){
            mIvCenter.setImageResource(R.drawable.icon_start);
            mIvCenter.setVisibility(GONE);
            mIvSwitch.setImageResource(R.drawable.icon_start);
        }else{
            mIvCenter.setImageResource(R.drawable.icon_pause);
            mIvCenter.setVisibility(VISIBLE);
            mIvSwitch.setImageResource(R.drawable.icon_pause);
        }
    }

    private void showLoadingView() {
    }

    public void setIsPauseClick(boolean click) {
        isRealPause = click;
    }

    public void setIsComplete(boolean complete) {
        isComplete = complete;
    }

    private void setCurrentPalyState(int state) {
        this.playState = state;
    }

    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    public int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {

    }

    public interface VidioPlayerListener {
        void onBufferUpdate(int time);

        void onClickFullScreen();

        void onClickVideo();

        void onVideoLoadSuccess();

        void onVideoPlayComplete();

        void onVideoLoadFail();
    }

    //监听屏幕是否锁屏接收器
    class ScreenEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //锁屏，解锁
            switch (intent.getAction()) {
                case Intent.ACTION_USER_PRESENT:
                    if (playState == STATE_PAUSEING) {
                        if (!isRealPause) {
                            decideCanPlay();
                        } else {
                            pause();
                        }
                    }
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    pause();
                    break;
            }
        }
    }
}
