package com.csz.video.media;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
    private Button mBtnPlay;
    private ImageView mIvFull;
    private ImageView mIvLoading;
    private ImageView mIvFrame;
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

    private boolean isPlaying() {
        return true;
    }

    public VideoLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

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
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

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

    public void setDataSource(String url){
        this.mUrl = url;
    }

    public void load(){

    }

    public void pause(){

    }

    public void resume(){}

    /**
     * 播放完成回到初始状态
     */
    public void playBack(){

    }

    public void stop(){

    }

    public void destory(){

    }

    public void seekAndResume(){}

    public void seekAndPause(){}

    public void setVidioPlayerListener(VidioPlayerListener listener){
        this.listener = listener;
    }

    private synchronized void checkMediaPlayer(){
        if (mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
    }

    private void registerBroadcastReceiver() {
        mScreenReceiver = new ScreenEventReceiver();
        //TODO
        getContext().registerReceiver(mScreenReceiver,new IntentFilter(Intent.ACTION_CREATE_SHORTCUT));
    }

    private void unRegisterReceiver(){
        if (mScreenReceiver != null){
            getContext().unregisterReceiver(mScreenReceiver);
        }
    }

    private void decideCanPlay(){}

    public void showFullBtn(boolean show){}

    public boolean isPauseBtnClicked(){
        return isRealPause;
    }

    public boolean isComplete(){return isComplete;}

    private void showPauseView(boolean show){

    }

    private void showLoadingView(){}

    private void showPlayView(){}

    private void loadFrameImage(){}

    private void setCurrentPalyState(int state){
        this.playState = state;
    }

    public int getDuration(){
        if (mediaPlayer != null){
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    public int getCurrentPosition(){
        if (mediaPlayer != null){
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {

    }

    interface VidioPlayerListener {
    }

    //监听屏幕是否锁屏接收器
    class ScreenEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
