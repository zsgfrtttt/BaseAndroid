package com.hydbest.baseandroid.activity.Media;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.hydbest.baseandroid.activity.Media.util.PcmToWavUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;

//所有安卓系统都支持
import static android.media.AudioFormat.CHANNEL_IN_MONO;
import static android.media.AudioFormat.ENCODING_PCM_16BIT;

/**
 * Created by csz on 2018/9/13.
 */

public class MediaAudioRecordActivity extends AppCompatActivity {

    private AudioRecord audioRecord = null;  // 声明 AudioRecord 对象
    private int recordBufSize = 0; // 声明recoordBufffer的大小字段
    private int frequency = 44100;

    private volatile boolean isRecording = false;
    private String fileName = Environment.getExternalStorageDirectory() + File.separator + "record.pcm";
    private String wavFileName = Environment.getExternalStorageDirectory() + File.separator + "record.wav";

    private MediaPlayer mPlayer;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.hydbest.baseandroid.R.layout.activity_media_audio_record);
        ButterKnife.bind(this);

        //initAudio();
    }

    private void initAudio() {
        //内部缓冲区大小
        recordBufSize = AudioRecord.getMinBufferSize(frequency, AudioFormat.CHANNEL_IN_MONO, ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,  AudioFormat.CHANNEL_IN_MONO, ENCODING_PCM_16BIT, recordBufSize);
    }

    public void record(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                ||ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    stop(null);
                    initAudio();
                    audioRecord.startRecording();
                    isRecording = true;
                    byte[] data = new byte[recordBufSize];
                    try {
                        int size = 0;
                        FileOutputStream fos = new FileOutputStream(fileName);
                        if (fos != null) {
                            while (isRecording) {
                                size = audioRecord.read(data, 0, recordBufSize);
                                if (size > 0) {
                                    fos.write(data,0,size);
                                } else {
                                    isRecording = false;
                                }
                            }
                            fos.flush();
                            fos.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void stop(View view) {
        isRecording = false;
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }

    }

    public void play(View view) {
        try {
            if (mPlayer != null) {
                mPlayer.stop();
                mPlayer.release();
            }
            File f = new File(fileName);
            if (f.exists()) {
                new PcmToWavUtil(frequency, CHANNEL_IN_MONO, ENCODING_PCM_16BIT).pcmToWav(fileName, wavFileName);
            }
            File wav = new File(wavFileName);
            if (wav.exists()) {
                mPlayer = new MediaPlayer();
                mPlayer.setDataSource(wavFileName);
                mPlayer.prepare();
                mPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
