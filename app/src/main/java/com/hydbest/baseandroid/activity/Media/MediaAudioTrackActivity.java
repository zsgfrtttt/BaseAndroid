package com.hydbest.baseandroid.activity.Media;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.hydbest.baseandroid.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * <p>
 * MODE_STREAM：在这种模式下，通过write一次次把音频数据写到AudioTrack中。
 * 这和平时通过write系统调用往文件中写数据类似，但这种工作方式每次都需要把数据从用户提供的Buffer中拷贝到AudioTrack内部的Buffer中，
 * 这在一定程度上会使引入延时。为解决这一问题，AudioTrack就引入了第二种模式。
 * MODE_STATIC：这种模式下，在play之前只需要把所有数据通过一次write调用传递到AudioTrack中的内部缓冲区，
 * 后续就不必再传递数据了。这种模式适用于像铃声这种内存占用量较小，延时要求较高的文件。
 * 但它也有一个缺点，就是一次write的数据不能太多，否则系统无法分配足够的内存来存储全部数据。
 * </p>
 */

public class MediaAudioTrackActivity extends AppCompatActivity {

    private String fileName = Environment.getExternalStorageDirectory() + File.separator + "record.wav";

    private AudioTrack mAudioTrack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_audio_track);
    }

    public void playStaticMode(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        FileInputStream fis = new FileInputStream(fileName);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        byte[] data = new byte[2048];
                        int size = 0;
                        while ((size = fis.read(data)) != -1) {
                            outputStream.write(data);
                        }
                        fis.close();
                        start(outputStream.toByteArray(), AudioTrack.MODE_STATIC);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void start(byte[] data, int mode) {
        if (mAudioTrack != null && mAudioTrack.getState() == AudioTrack.PLAYSTATE_PAUSED) {
            mAudioTrack.play();
            return;
        } else if (mAudioTrack != null) {
            mAudioTrack.stop();
            mAudioTrack.release();
        }
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                data.length, mode);
        mAudioTrack.write(data, 0, data.length);
        mAudioTrack.play();
    }

    public void playStreamMode(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mAudioTrack != null && mAudioTrack.getState() == AudioTrack.PLAYSTATE_PAUSED) {
                            mAudioTrack.play();
                            return;
                        } else if (mAudioTrack != null) {
                            mAudioTrack.stop();
                            mAudioTrack.release();
                        }
                        int bufferSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
                        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                                Math.max(2048, bufferSize), AudioTrack.MODE_STREAM);

                        FileInputStream fis = new FileInputStream(fileName);
                        byte[] data = new byte[2048];
                        int size = 0;
                        int status;
                        boolean error = false;
                        while ((size = fis.read(data)) != -1 && !error) {
                            status = mAudioTrack.write(data, 0, size);
                            switch (status) {
                                case AudioTrack.ERROR_INVALID_OPERATION:
                                case AudioTrack.ERROR_BAD_VALUE:
                                case AudioTrack.ERROR_DEAD_OBJECT:
                                    error = true;
                                    break;
                            }
                            if (!error) {
                                mAudioTrack.play();
                            }
                        }
                        fis.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }
}
