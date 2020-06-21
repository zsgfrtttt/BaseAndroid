package com.hydbest.baseandroid.activity.Media;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hydbest.baseandroid.R;
import com.hydbest.player.media.IjkVideoView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class IjkPlayerActivity extends AppCompatActivity implements GestureDetectorController.OnGestureListener {

    private static final int TIME_INTERVAL = 5000;
    private static final int TIME_SECOND = 1000;

    private static final int MSG_HIDE_PANE = 1;
    private static final int MSG_BATTERY = 2;
    private static final int MSG_UPDATE_SYS_TIME = 3;
    private static final int MSG_UPDATE_PLAY_TIME = 4;

    private RelativeLayout mLoadingLayout;
    private TextView mTvLoading;
    private IjkVideoView mVideoView;

    private LinearLayout pane_top;
    private LinearLayout pane_bottom;
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_sys_time;
    private ImageView iv_start_or_pause;
    private ImageView iv_next;
    private TextView tv_play_time;
    private SeekBar seek;
    private TextView tv_duration_time;
    private RelativeLayout rl_attributes;
    private TextView tv_attr;

    //湖南rtmp://58.200.131.2:1935/livetv/hunantv
    //rtmp://202.69.69.180:443/webcast/bshdlive-pc
    //rtmp://58.200.131.2:1935/livetv/hunantv
    private String url = "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4";
    private String liveUrl = "rtmp://58.200.131.2:1935/livetv/hunantv";
    private boolean mPrepare;
    private boolean mPaneShow;
    private boolean mIsPlaying;
    private boolean mIsHorzontailScroll;
    private long mTouchTime;
    private Receiver mReceiver = new Receiver();
    private StringBuilder mStringBuilder = new StringBuilder();
    private Formatter mFormatter;
    private GestureDetectorController mGestureDetectorController;
    private AudioManager mAudioManager;

    private int mMaxVolume;
    private int mVolume;
    private float mBrightness;
    private float mVolumeRadio;
    private float mHorizontailRaio;
    private int mProgress;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_HIDE_PANE:
                    hidePaneLayout();
                    break;
                case MSG_BATTERY:
                    Log.i("csz", "battery is " + msg.obj);
                    break;
                case MSG_UPDATE_SYS_TIME:
                    updateSysTime();
                    break;
                case MSG_UPDATE_PLAY_TIME:
                    updatePlayTime();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ijkplayer);

        initArgument();
        initView();
        initEvent();
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mVideoView.setVideoURI(Uri.parse(liveUrl));
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mIsPlaying = true;
                mPrepare = true;
                mVideoView.start();
                seek.setMax(mVideoView.getDuration());
                showPaneLayout();
            }
        });
        mVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                switch (what) {
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        mLoadingLayout.setVisibility(View.VISIBLE);
                        break;
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    case IjkMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    case IjkMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                        mLoadingLayout.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });
    }

    private void initArgument() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mAudioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mVolumeRadio = mVolume * 1.f / mMaxVolume;
        try {
            mBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS) / 255.f;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mVideoView = findViewById(R.id.video_view);
        mTvLoading = findViewById(R.id.tv_load);
        mLoadingLayout = findViewById(R.id.rl_loading_layout);
        pane_top = findViewById(R.id.pane_top);
        pane_bottom = findViewById(R.id.pane_bottom);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_sys_time = findViewById(R.id.tv_sys_time);

        iv_start_or_pause = findViewById(R.id.iv_start_or_pause);
        iv_next = findViewById(R.id.iv_next);
        tv_play_time = findViewById(R.id.tv_play_time);
        seek = findViewById(R.id.seek);
        tv_duration_time = findViewById(R.id.tv_duration_time);

        rl_attributes = findViewById(R.id.rl_attributes);
        tv_attr = findViewById(R.id.tv_attr);

        mFormatter = new Formatter(mStringBuilder, Locale.getDefault());
        mGestureDetectorController = new GestureDetectorController(this, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mTouchTime = SystemClock.elapsedRealtime();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mIsHorzontailScroll) {
                mIsHorzontailScroll = false;
                mHorizontailRaio = 0f;
                mVideoView.seekTo(seek.getProgress());
                updatePlayTime();
            }
            long interval = SystemClock.elapsedRealtime() - mTouchTime;
            if (interval < 300) {
                if (mPaneShow) {
                    hidePaneLayout();
                } else {
                    showPaneLayout();
                }
            }
            if (rl_attributes.getVisibility() == View.VISIBLE) {
                rl_attributes.setVisibility(View.GONE);
            }
        }
        return mGestureDetectorController.onTouchEvent(event);
    }

    private void initEvent() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseVideo();
            }
        });
        iv_start_or_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleStartOrPause();
            }
        });
        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaneLayout();
                Toast.makeText(IjkPlayerActivity.this, "没有下一集", Toast.LENGTH_SHORT).show();
            }
        });
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mIsHorzontailScroll){
                    tv_play_time.setText(formatMillionTime(progress));
                }
                if (!fromUser || !mPrepare) {
                    return;
                }
                tv_play_time.setText(formatMillionTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeMessages(MSG_HIDE_PANE);
                mHandler.removeMessages(MSG_UPDATE_PLAY_TIME);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mVideoView.seekTo(seekBar.getProgress());
                showPaneLayout();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (iv_start_or_pause.isSelected()) {
            mVideoView.resume();
            mIsPlaying = true;
            mAudioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        mIsPlaying = false;
        mVideoView.pause();
        mAudioManager.abandonAudioFocus(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseVideo();
        mHandler.removeCallbacksAndMessages(null);
    }

    private void releaseVideo() {
        if (!mPrepare) {
            return;
        }
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            mVideoView.release(true);
        } else {
            mVideoView.release(true);
        }
        finish();
    }

    private void handleStartOrPause() {
        if (!mPrepare) {
            return;
        }
        showPaneLayout();
        if (mVideoView.isPlaying()) {
            mIsPlaying = false;
            mVideoView.pause();
            mHandler.removeMessages(MSG_UPDATE_PLAY_TIME);
            iv_start_or_pause.setSelected(true);
            iv_start_or_pause.refreshDrawableState();
        } else {
            mIsPlaying = true;
            mVideoView.start();
            updatePlayTime();
            iv_start_or_pause.setSelected(false);
            iv_start_or_pause.refreshDrawableState();
        }
    }

    private void showPaneLayout() {
        updateSysTime();
        updatePlayTime();
        mPaneShow = true;
        pane_top.setVisibility(View.VISIBLE);
        pane_bottom.setVisibility(View.VISIBLE);
        mHandler.removeMessages(MSG_HIDE_PANE);
        mHandler.sendMessageDelayed(Message.obtain(mHandler, MSG_HIDE_PANE), TIME_INTERVAL);
    }

    private void updateSysTime() {
        tv_sys_time.setText(getCurrentTime());
        mHandler.removeMessages(MSG_UPDATE_SYS_TIME);
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_SYS_TIME, TIME_SECOND);
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date());
    }

    private void hidePaneLayout() {
        mHandler.removeMessages(MSG_UPDATE_SYS_TIME);
        mHandler.removeMessages(MSG_UPDATE_PLAY_TIME);
        mPaneShow = false;
        pane_bottom.setVisibility(View.GONE);
        pane_top.setVisibility(View.GONE);
    }

    private void updatePlayTime() {
        if (!mPrepare) return;
        int duration = mVideoView.getDuration();
        int current = mVideoView.getCurrentPosition();
        tv_duration_time.setText(formatMillionTime(duration));
        tv_play_time.setText(formatMillionTime(current));
        seek.setProgress(current);
        seek.setSecondaryProgress(mVideoView.getBufferPercentage() * seek.getMax() / 100);
        if (mIsPlaying) {
            mHandler.removeMessages(MSG_UPDATE_PLAY_TIME);
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_PLAY_TIME, TIME_SECOND);
        }
    }

    private String formatMillionTime(long time) {
        String target = null;
        if (time < 0) {
            return "00:00";
        }
        time = time / 1000;
        long second = time % 60;
        long min = (time / 60) % 60;
        long hour = time / 3600;
        mStringBuilder.setLength(0);
        if (hour > 0) {
            target = mFormatter.format("%d:%02d:%02d", hour, min, second).toString();
        } else {
            target = mFormatter.format("%02d:%02d", min, second).toString();
        }
        return target;
    }

    @Override
    public void onLeftVerticalScroll(float distance) {
        if (!mPrepare) return;
        int height = mVideoView.getHeight();
        mBrightness = (distance / height) + mBrightness;
        mBrightness = Math.max(0, Math.min(1.f, mBrightness));
        mStringBuilder.setLength(0);

        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.screenBrightness = mBrightness;
        getWindow().setAttributes(attributes);
        String target = mFormatter.format(Locale.getDefault(), "%02d", Math.round(mBrightness * 100)).toString();
        composeDrawableAndText(R.drawable.ic_brightness, "%" + target);
    }

    @Override
    public void onRightVerticalScroll(float offsetY) {
        if (!mPrepare) return;
        int height = mVideoView.getHeight();
        mVolumeRadio = (offsetY / height) + mVolumeRadio;
        mVolumeRadio = Math.max(0, Math.min(1.f, mVolumeRadio));
        mVolume = Math.round(mVolumeRadio * mMaxVolume);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,mVolume,0);
        mStringBuilder.setLength(0);
        String target = mFormatter.format(Locale.getDefault(), "%02d", Math.round(mVolumeRadio * 100)).toString();
        composeDrawableAndText(R.drawable.ic_volume, "%" + target);
    }

    private void composeDrawableAndText(int drawableId, String s) {
        rl_attributes.setVisibility(View.VISIBLE);
        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tv_attr.setText(s);
        tv_attr.setCompoundDrawables(null, drawable, null, null);
    }

    @Override
    public void onHorizontalScroll(float distance) {
        if (!mPrepare) {
            return;
        }
        if (!mIsHorzontailScroll){
            mProgress = mVideoView.getCurrentPosition();
        }
        mIsHorzontailScroll = true;
        int width = mVideoView.getWidth();
        long time = 16000;
        mHorizontailRaio = -(distance / width) + mHorizontailRaio;
        mHandler.removeMessages(MSG_UPDATE_PLAY_TIME);
        int progress = (int) (mHorizontailRaio * time + mProgress);
        progress = Math.max(0,Math.min(seek.getMax(),progress));
        seek.setProgress(progress);
    }

    private class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 100);
            Message.obtain(mHandler, MSG_BATTERY, level).sendToTarget();
        }
    }

}
