package com.hydbest.baseandroid.activity.Media;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MediaVoiceActivity extends AppCompatActivity{
    private SoundPool mSoundPool;
    private int mId ;
    private int mStreamId ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.hydbest.baseandroid.R.layout.activity_media_voice);
        initSoundPool();
    }

    private void initSoundPool(){
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSoundPool = new SoundPool(streamMaxVolume, AudioManager.STREAM_MUSIC,0);
        mId = mSoundPool.load(this, com.hydbest.baseandroid.R.raw.ring,0);
    }

    public void spPlay(View view){
        mStreamId = mSoundPool.play(mId,1.f,1.f,0,0,1.f);
    }

    public void spStop(View view){
        mSoundPool.stop(mStreamId);
    }


}
