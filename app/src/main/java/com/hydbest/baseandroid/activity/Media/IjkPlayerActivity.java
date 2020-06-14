package com.hydbest.baseandroid.activity.Media;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hydbest.baseandroid.R;
import com.hydbest.player.media.IjkVideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class IjkPlayerActivity extends AppCompatActivity {
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

    private String url = "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ijkplayer);


        initView();
        initEvent();

        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mVideoView.setVideoURI(Uri.parse(url));
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mVideoView.start();
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
        seek.setProgress(50);
        tv_duration_time = findViewById(R.id.tv_duration_time);
    }

    private void initEvent() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        iv_start_or_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
}
