package com.csz.video.media;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.csz.video.R;

import java.io.IOException;

public class VideoLayout extends FrameLayout implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, TextureView.SurfaceTextureListener {

    private static final int TIME_MSG = 0x01;
    private static final int SENSOR_MSG = 0x02;
    private static final int TIME_INVAL = 1000;
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_PAUSEING = 2;
    private static final int LOAD_TOTAL_COUNT = 1;

    private ViewGroup mParentContainer;
    private RelativeLayout mPlayerView;
    private NiceTextureView mNiceTextureView;
    private ImageView mIvPreview;
    private ImageView mIvCenter;
    private ImageView mIvFull;
    private ImageView mIvSwitch;
    private ProgressBar mProgressBar;
    private LinearLayout mLayoutBottom;
    private SeekBar mSeekBar;
    private AudioManager mAudioManager;

    private SurfaceTexture mSurfaceTexture;
    private Surface videoSurface;

    private String mUrl;
    private boolean isMute;
    private boolean isList = true;
    private boolean mAutoPlay;
    private int mOriginOritation;//原Activity设置的方向
    private boolean mStopPostMsg;//是否停止发送消息
    private int mSreenWidth, mDestationHeight;
    private int mPortraitHeight;
    private int mPortraitWidth;

    private boolean isRealPause;
    private boolean isComplete;
    private boolean isFullScreen;
    private int mCurrentCount;
    private int playState = STATE_IDLE;

    private MediaPlayer mediaPlayer;
    private VideoHandler mVideoHandler;
    private VidioPlayerListener listener;
    private OrientationEventListener mOrientationListener; // 屏幕方向改变监听器
    private ScreenEventReceiver mScreenReceiver;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_MSG:
                    if (isPlaying() && !mStopPostMsg) {
                        listener.onBufferUpdate(getCurrentPosition());
                        mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                        sendEmptyMessageDelayed(TIME_MSG, TIME_INVAL);
                    }
                    break;
                case SENSOR_MSG:
                    //开启自动旋转，响应屏幕旋转事件ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
                    Util.scanForActivity(getContext()).setRequestedOrientation(mOriginOritation);
                    break;
            }
        }
    };

    public VideoLayout(Context context, ViewGroup parentContainer, VideoHandler handler, boolean autoPlay, int oritation) {
        super(context);
        mParentContainer = parentContainer;
        mVideoHandler = handler;
        mAutoPlay = autoPlay;
        mOriginOritation = oritation;
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        setId(R.id.video_layout);
        initData();
        initView();
        //startConfigListener();
        registerBroadcastReceiver();
    }

    private void initData() {
        mSreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mDestationHeight = mSreenWidth * 9 / 16;

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                mPortraitHeight = getHeight();
                mPortraitWidth = getWidth();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void initView() {
        View.inflate(getContext(), R.layout.layout_video, this);
        mNiceTextureView = new NiceTextureView(getContext());
        mIvPreview = findViewById(R.id.iv_preview);
        mIvCenter = findViewById(R.id.iv_center);
        mIvFull = findViewById(R.id.iv_full);
        mIvSwitch = findViewById(R.id.iv_switch);
        mProgressBar = findViewById(R.id.progress);
        mLayoutBottom = findViewById(R.id.layout_bottom);
        mSeekBar = findViewById(R.id.seek);
        addTextureView();

        mIvCenter.setOnClickListener(this);
        mIvFull.setOnClickListener(this);
        mIvSwitch.setOnClickListener(this);
        mNiceTextureView.setSurfaceTextureListener(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacksAndMessages(null);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (playState == STATE_PAUSEING) {
                    seekAndPause(seekBar.getProgress());
                } else if (playState == STATE_PLAYING) {
                    seekAndResume(seekBar.getProgress());
                }
            }
        });

        showPauseOrPlayView(false);
    }

    private void addTextureView() {
        removeView(mNiceTextureView);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        addView(mNiceTextureView, 0, params);
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mSeekBar.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (listener != null) {
            listener.onVideoPlayComplete();
        }
        mCurrentCount = 0;
        setCurrentPalyState(STATE_PAUSEING);
        setIsComplete(true);
        setIsPauseClick(true);
        playBack();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // 直播流播放时去调用mediaPlayer.getDuration会导致-38和-2147483648错误，忽略该错误
        if (what != -38 && what != -2147483648 && extra != -38 && extra != -2147483648) {
            Log.i("csz","eeee");
            if (mCurrentCount >= LOAD_TOTAL_COUNT) {
                if (listener != null) {
                    listener.onVideoLoadFail();
                }
                stop();
                mCurrentCount = 0;
            } else {
                mCurrentCount += 1;
                setCurrentPalyState(STATE_IDLE);
                load();
            }
        }
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer = mp;
        if (mediaPlayer != null) {
            mediaPlayer.setOnBufferingUpdateListener(this);
            mCurrentCount = 0;
            if (listener != null) {
                listener.onVideoLoadSuccess();
            }
            setCurrentPalyState(STATE_PAUSEING);
            decideCanPlay();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (videoSurface == null) {
            mSurfaceTexture = surface;
            videoSurface = new Surface(surface);
            if (mAutoPlay)
                load();
        } else {
            mNiceTextureView.setSurfaceTexture(mSurfaceTexture);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mIvPreview.setVisibility(VISIBLE);
        return videoSurface == null;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE && playState == STATE_PAUSEING) {
            if (isRealPause || isComplete()) {
                pause(isRealPause);
            } else {
                decideCanPlay();
            }
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
            VideoManager.getInstance().setCurrentPlayHandler(mVideoHandler);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    public void resume() {
        hideLoadingView();
        if (playState != STATE_PLAYING && playState != STATE_PAUSEING) {
            return;
        }
        if (!isPlaying()) {
            entryResumeState();
            showPauseOrPlayView(true);
            mediaPlayer.start();
            mSeekBar.setMax(mediaPlayer.getDuration());
            mHandler.sendEmptyMessage(TIME_MSG);
            mStopPostMsg = false;
        }
    }

    /**
     * 播放 非主动暂停
     */
    public void resumeByFilter() {
        if (!isRealPause) {
            resume();
        }
    }

    /**
     * 暂停播放
     *
     * @param isRealPause true:需要手动开启     false:view可见时自动开启播放
     */
    public void pause(boolean isRealPause) {
        hideLoadingView();
        if (playState != STATE_PLAYING && playState != STATE_PAUSEING) {
            return;
        }
        setCurrentPalyState(STATE_PAUSEING);
        setIsPauseClick(isRealPause);
        if (isPlaying()) {
            mediaPlayer.pause();
        }
        showPauseOrPlayView(false);
        mStopPostMsg = true;
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
        mStopPostMsg = true;
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
            mediaPlayer.setOnSeekCompleteListener(null);
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mIvPreview.setVisibility(VISIBLE);
        hideLoadingView();
        showPauseOrPlayView(false);
        setCurrentPalyState(STATE_IDLE);
        mStopPostMsg = true;
        mHandler.removeCallbacksAndMessages(null);
    }

    private void entryResumeState() {
        setCurrentPalyState(STATE_PLAYING);
        setIsPauseClick(false);
        setIsComplete(false);
    }

    public void seekAndResume(int position) {
        if (playState != STATE_PLAYING && playState != STATE_PAUSEING) {
            return;
        }
        if (mediaPlayer.isPlaying() || mediaPlayer.getCurrentPosition() > 0) {
            mediaPlayer.seekTo(position);
            mStopPostMsg = true;
            mHandler.removeCallbacksAndMessages(null);
            showLoadingView();
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    decideCanPlay();
                    hideLoadingView();
                }
            });

        }
    }

    public void seekAndPause(int position) {
        if (playState != STATE_PLAYING && playState != STATE_PAUSEING) {
            return;
        }
        setCurrentPalyState(STATE_PAUSEING);
        showPauseOrPlayView(false);
        mediaPlayer.seekTo(position);
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                mediaPlayer.pause();
                mStopPostMsg = true;
                mHandler.removeCallbacksAndMessages(null);
            }
        });

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
            player.setOnInfoListener(mOnInfoListener);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnBufferingUpdateListener(this);
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
        unRegisterReceiver();
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
            resume();
        } else {
            pause(false);
        }
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
        if (play) {
            mIvCenter.setImageResource(R.drawable.icon_start);
            mIvCenter.setVisibility(GONE);
            mIvSwitch.setImageResource(R.drawable.icon_start);
        } else {
            mIvCenter.setImageResource(R.drawable.icon_pause);
            mIvCenter.setVisibility(VISIBLE);
            mIvSwitch.setImageResource(R.drawable.icon_pause);
        }
    }

    private void showLoadingView() {
        mIvCenter.setVisibility(GONE);
        mProgressBar.setVisibility(VISIBLE);
    }

    private void hideLoadingView() {
        mProgressBar.setVisibility(GONE);
        mIvPreview.setVisibility(GONE);
    }

    public void onFullScreen(boolean isFull) {
        if (isFull) {
            mIvFull.setImageResource(R.drawable.icon_zoom_small);
            Window window = Util.scanForActivity(getContext()).getWindow();
            //window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            VideoManager.getInstance().setCurrentPlayHandler(mVideoHandler);
        } else {
            mIvFull.setImageResource(R.drawable.icon_full);
            Window window = Util.scanForActivity(getContext()).getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
        isFullScreen = isFull;
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

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public VideoHandler getVideoHandler() {
        return mVideoHandler;
    }

    @Override
    public void onClick(View v) {
        if (v == mIvCenter) {
            if (playState == STATE_IDLE) {
                load();
            } else if (playState == STATE_PAUSEING) {
                decideCanPlay();
            }
        } else if (v == mIvSwitch) {
            if (playState == STATE_IDLE) {
                load();
            } else if (playState == STATE_PAUSEING) {
                decideCanPlay();
            } else {
                pause(true);
            }
        } else if (v == mIvFull) {
            changeConfiguration();
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isList) return;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setScaleView(true);
        } else {
            setScaleView(false);
        }
    }

    private void setScaleView(boolean isFullScreen) {
        if (isFullScreen) {
            Util.hideActionBar(getContext());
            ViewGroup contentView = Util.scanForActivity(getContext()).findViewById(android.R.id.content);
            if (getParent() != null) {
                ((ViewGroup) getParent()).removeView(this);
            }
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            contentView.addView(this, params);
            onFullScreen(true);
        } else {
            Util.showActionBar(getContext());
            ViewGroup contentView = Util.scanForActivity(getContext()).findViewById(android.R.id.content);
            contentView.removeView(this);
            mParentContainer.removeView(this);
            mParentContainer.addView(this);
            onFullScreen(false);
        }

    }

    /**
     * 横竖屏切换
     */
    private void changeConfiguration() {
        if (listener != null) {
            if (isFullScreen) {
                listener.onExitFullScreen();
            } else {
                listener.onClickFullScreen();
            }
        }
    }

    public void sendRequestOritationMsg(){
        mHandler.sendEmptyMessageDelayed(SENSOR_MSG, 5000);
    }

    private void startConfigListener() {
        mOrientationListener = new OrientationEventListener(getContext()) {
            @Override
            public void onOrientationChanged(int orientation) {
                if ((orientation > 45 && orientation < 135) || (orientation > 225 && orientation < 315)) {
                } else {
                }
            }
        };
        mOrientationListener.enable();
    }

    public interface VidioPlayerListener {
        void onBufferUpdate(int time);

        void onClickFullScreen();

        void onExitFullScreen();

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
                            pause(true);
                        }
                    }
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    pause(isRealPause);
                    break;
            }
        }
    }

    private MediaPlayer.OnInfoListener mOnInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                // 播放器开始渲染
            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                // MediaPlayer暂时不播放，以缓冲更多的数据
                if (playState == STATE_PAUSEING || playState == STATE_PLAYING){
                    showLoadingView();
                }
            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                // 填充缓冲区后，MediaPlayer恢复播放/暂停
                hideLoadingView();
                if (playState == STATE_PAUSEING || playState == STATE_PLAYING){
                    if (isRealPause){
                        pause(isRealPause);
                    }else{
                        resume();
                    }
                }
            } else if (what == MediaPlayer.MEDIA_INFO_NOT_SEEKABLE) {
                Log.i("csz","视频不能seekTo，为直播视频");
            } else {

            }
            return false;
        }
    };
}
