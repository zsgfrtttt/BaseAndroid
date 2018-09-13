package com.hydbest.baseandroid.activity.Media;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.activity.Media.util.PcmToWavUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;

import static android.media.AudioFormat.CHANNEL_IN_DEFAULT;
import static android.media.AudioFormat.ENCODING_PCM_16BIT;

/**
 * Created by csz on 2018/9/13.
 */

public class MediaAudioRecordActivity extends AppCompatActivity {
    private AudioRecord audioRecord = null;  // 声明 AudioRecord 对象
    private int recordBufSize = 0; // 声明recoordBufffer的大小字段
    private int frequency = 10000;

    private volatile boolean isRecording = false;
    private String fileName = Environment.getExternalStorageDirectory() + File.separator + "record.pcm";
    private String wavFileName = Environment.getExternalStorageDirectory() + File.separator + "record.wav";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_audio_record);
        ButterKnife.bind(this);

        //initAudio();
    }

    private void initAudio() {
        recordBufSize = AudioRecord.getMinBufferSize(frequency, CHANNEL_IN_DEFAULT, ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, CHANNEL_IN_DEFAULT, ENCODING_PCM_16BIT, recordBufSize);
    }

    public void record(View view) {
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
                            if (size != AudioRecord.ERROR_INVALID_OPERATION) {
                                fos.write(data);
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

    public void stop(View view) {
        isRecording = false;
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }

    }

    public void play(View view) {
        File f = new File(fileName);
        if (f.exists()) {
            new PcmToWavUtil(frequency, CHANNEL_IN_DEFAULT, ENCODING_PCM_16BIT).pcmToWav(fileName, wavFileName);
        }
        File wav = new File(wavFileName);
        if (wav.exists()) {

        }
    }
}
