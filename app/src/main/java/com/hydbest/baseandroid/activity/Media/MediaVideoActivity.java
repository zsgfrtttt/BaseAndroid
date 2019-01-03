package com.hydbest.baseandroid.activity.Media;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaVideoActivity extends AppCompatActivity{
    @BindView(com.hydbest.baseandroid.R.id.video_view)
    VideoView videoView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(com.hydbest.baseandroid.R.layout.activity_media_video);
        ButterKnife.bind(this);
    }

    public void play(View view){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                 || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            videoView.setVideoPath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"好想你.mp4");  // 香港卫视地址
            videoView.start();
            MediaController mediaController = new MediaController(this);
            videoView.setMediaController(mediaController);
            mediaController.setMediaPlayer(videoView);
        }
    }

    public void stop(View view){
        if (videoView.isPlaying()) {
            videoView.pause();
        }
    }
}
