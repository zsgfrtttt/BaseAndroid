package com.hydbest.baseandroid.activity.Media;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hydbest.baseandroid.R;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;


/**
 * Created by csz on 2018/9/14.
 * 参考 http://www.cnblogs.com/renhui/p/7474096.html
 * http://vivianking6855.github.io/2017/06/19/Android-Vedio-merge-Music/
 */

public class MediaExecutorActivity extends AppCompatActivity {
    private static final String TAG = "csz";

    private String sdcard_path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    private long videoSampleTime;
    private long audioSampleTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_executor);
    }

    @TargetApi(16)
    public void mediaExtractor(View view) {
        if (ActivityCompat.checkSelfPermission(MediaExecutorActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MediaExecutorActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MediaExecutorActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileOutputStream videoOutputStream = null;
                    FileOutputStream audioOutputStream = null;
                    MediaExtractor mediaExtractor = new MediaExtractor();
                    try {
                        //分离的视频文件
                        File videoFile = new File(sdcard_path, "分离的视频块.h264");
                        //分离的音频文件
                        File audioFile = new File(sdcard_path, "分离的音频块.aac");
                        videoOutputStream = new FileOutputStream(videoFile);
                        audioOutputStream = new FileOutputStream(audioFile);
                        //输入文件,也可以是网络文件
                        //oxford.mp4 视频 h264/baseline  音频 aac/lc 44.1k  2 channel 128kb/s
                        mediaExtractor.setDataSource(sdcard_path + "/倒立.mp4");
                        //test3.mp4  视频h264 high   音频aac
                        //        mediaExtractor.setDataSource(sdcard_path + "/test3.mp4");
                        //test2.mp4 视频mpeg4  音频MP3
                        //  mediaExtractor.setDataSource(sdcard_path + "/test2.mp4");
                        //信道总数
                        int trackCount = mediaExtractor.getTrackCount();
                        Log.d(TAG, "trackCount:" + trackCount);
                        int audioTrackIndex = -1;
                        int videoTrackIndex = -1;
                        for (int i = 0; i < trackCount; i++) {
                            MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
                            String mineType = trackFormat.getString(MediaFormat.KEY_MIME);
                            //视频信道
                            if (mineType.startsWith("video/")) {
                                videoTrackIndex = i;
                            }
                            //音频信道
                            if (mineType.startsWith("audio/")) {
                                audioTrackIndex = i;
                            }
                        }

                        ByteBuffer byteBuffer = ByteBuffer.allocate(500 * 1024);
                        //切换到视频信道
                        mediaExtractor.selectTrack(videoTrackIndex);
                        while (true) {
                            int readSampleCount = mediaExtractor.readSampleData(byteBuffer, 0);
                            Log.d(TAG, "video:readSampleCount:" + readSampleCount);
                            if (readSampleCount < 0) {
                                break;
                            }
                            //保存视频信道信息
                            byte[] buffer = new byte[readSampleCount];
                            byteBuffer.get(buffer);
                            videoOutputStream.write(buffer);//buffer 写入到 videooutputstream中
                            byteBuffer.clear();
                            mediaExtractor.advance();
                        }
                        //切换到音频信道
                        mediaExtractor.selectTrack(audioTrackIndex);
                        while (true) {
                            int readSampleCount = mediaExtractor.readSampleData(byteBuffer, 0);
                            Log.d(TAG, "audio:readSampleCount:" + readSampleCount);
                            if (readSampleCount < 0) {
                                break;
                            }
                            //保存音频信息
                            byte[] buffer = new byte[readSampleCount];
                            byteBuffer.get(buffer);
                            /************************* 用来为aac添加adts头**************************/
                            byte[] aacaudiobuffer = new byte[readSampleCount + 7];
                            addADTStoPacket(aacaudiobuffer, readSampleCount + 7);
                            System.arraycopy(buffer, 0, aacaudiobuffer, 7, readSampleCount);
                            audioOutputStream.write(aacaudiobuffer);
                            /***************************************close**************************/
                            //  audioOutputStream.write(buffer);
                            byteBuffer.clear();
                            mediaExtractor.advance();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MediaExecutorActivity.this, "分离视频成功", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (final Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MediaExecutorActivity.this, "分离出现错误" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } finally {
                        Log.d(TAG, "mediaExtractor.release!\n");
                        mediaExtractor.release();
                        mediaExtractor = null;
                        try {
                            videoOutputStream.close();
                            audioOutputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

    }

    @TargetApi(18)
    public void mediaMuxer(View view) {
        if (ActivityCompat.checkSelfPermission(MediaExecutorActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MediaExecutorActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MediaExecutorActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MediaExtractor mediaAudioExtractor = new MediaExtractor();
                        MediaExtractor mediaVideoExtractor = new MediaExtractor();
                        MediaMuxer mMediaMuxer = new MediaMuxer(sdcard_path + "合成后的视频.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

                        mediaAudioExtractor.setDataSource(sdcard_path + "张国荣.mp4");
                        mediaVideoExtractor.setDataSource(sdcard_path + "好想你.mp4");

                        int mAudioTrackIndex = 0;
                        for (int i = 0; i<mediaAudioExtractor.getTrackCount();i++){
                            MediaFormat trackFormat = mediaAudioExtractor.getTrackFormat(i);
                            String mineType = trackFormat.getString(MediaFormat.KEY_MIME);
                            if (mineType.startsWith("audio/")) {
                                //合成音频
                                mAudioTrackIndex = mMediaMuxer.addTrack(trackFormat);
                            }
                        }
                        for (int i = 0; i < mediaVideoExtractor.getTrackCount(); i++) {
                            MediaFormat trackFormat = mediaVideoExtractor.getTrackFormat(i);
                            String mineType = trackFormat.getString(MediaFormat.KEY_MIME);
                            if (mineType.startsWith("video/")) {
                                //合成视频
                                int mVideoTrackIndex = mMediaMuxer.addTrack(trackFormat);
                                mMediaMuxer.start();
                                MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                                info.presentationTimeUs = 0;
                                ByteBuffer buffer = ByteBuffer.allocate(500 * 1024);
                                int sampleSize = 0;
                                mediaVideoExtractor.selectTrack(i);

                                videoSampleTime = getSampleTime(mediaVideoExtractor,buffer,i);

                                mediaVideoExtractor.unselectTrack(i);
                                mediaVideoExtractor.selectTrack(i);
                                while ((sampleSize = mediaVideoExtractor.readSampleData(buffer, 0)) > 0) {
                                    info.offset = 0;
                                    info.size = sampleSize;
                                    info.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
                                    info.presentationTimeUs += videoSampleTime;
                                    mMediaMuxer.writeSampleData(mVideoTrackIndex, buffer, info);
                                    mediaVideoExtractor.advance();
                                }
                            }
                        }

                        for (int i = 0; i < mediaAudioExtractor.getTrackCount(); i++) {
                            MediaFormat trackFormat = mediaAudioExtractor.getTrackFormat(i);
                            String mineType = trackFormat.getString(MediaFormat.KEY_MIME);
                            if (mineType.startsWith("audio/")) {
                                //合成音频
                                MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                                info.presentationTimeUs = 0;
                                ByteBuffer buffer = ByteBuffer.allocate(500 * 1024);
                                int sampleSize = 0;
                                mediaAudioExtractor.selectTrack(i);

                                audioSampleTime = getSampleTime(mediaAudioExtractor,buffer,i);
                                while ((sampleSize = mediaAudioExtractor.readSampleData(buffer, 0)) > 0) {
                                    info.offset = 0;
                                    info.size = sampleSize;
                                    info.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
                                    info.presentationTimeUs += audioSampleTime;
                                    mMediaMuxer.writeSampleData(mAudioTrackIndex, buffer, info);
                                    mediaAudioExtractor.advance();
                                }
                            }

                        }

                        mediaAudioExtractor.release();
                        mediaVideoExtractor.release();
                        if (mMediaMuxer != null) {
                            mMediaMuxer.stop();
                            mMediaMuxer.release();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MediaExecutorActivity.this, "合成终于成功", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (final Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MediaExecutorActivity.this, "合成出现错误" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }).start();
        }

    }


    /**
     * 获取每帧的之间的时间
     *
     * @return
     */
    @TargetApi(18)
    private long getSampleTime(MediaExtractor mediaExtractor, ByteBuffer byteBuffer, int videoTrack) {
        if (mediaExtractor == null) {
            Log.w(TAG, "getSampleTime mediaExtractor is null");
            return 0;
        }
        mediaExtractor.readSampleData(byteBuffer, 0);
        //skip first I frame
        if (mediaExtractor.getSampleFlags() == MediaExtractor.SAMPLE_FLAG_SYNC) {
            mediaExtractor.advance();
        }
        mediaExtractor.readSampleData(byteBuffer, 0);

        // get first and second and count sample time
        long firstVideoPTS = mediaExtractor.getSampleTime();
        mediaExtractor.advance();
        mediaExtractor.readSampleData(byteBuffer, 0);
        long SecondVideoPTS = mediaExtractor.getSampleTime();
        long sampleTime = Math.abs(SecondVideoPTS - firstVideoPTS);
        Log.d(TAG, "getSampleTime is " + sampleTime);

        // 重新切换此信道，不然上面跳过了3帧,造成前面的帧数模糊
        mediaExtractor.unselectTrack(videoTrack);
        mediaExtractor.selectTrack(videoTrack);

        return sampleTime;
    }


    /**
     * 这里之前遇到一个坑，以为这个packetLen是adts头的长度，也就是7，仔细看了下代码，发现这个不是adts头的长度，而是一帧音频的长度
     *
     * @param packet    一帧数据（包含adts头长度）
     * @param packetLen 一帧数据（包含adts头）的长度
     */
    private static void addADTStoPacket(byte[] packet, int packetLen) {
        int profile = 2; // AAC LC
        int freqIdx = getFreqIdx(44100);
        int chanCfg = 2; // CPE

        // fill in ADTS data
        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF9;
        packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;
    }


    private static int getFreqIdx(int sampleRate) {
        int freqIdx;

        switch (sampleRate) {
            case 96000:
                freqIdx = 0;
                break;
            case 88200:
                freqIdx = 1;
                break;
            case 64000:
                freqIdx = 2;
                break;
            case 48000:
                freqIdx = 3;
                break;
            case 44100:
                freqIdx = 4;
                break;
            case 32000:
                freqIdx = 5;
                break;
            case 24000:
                freqIdx = 6;
                break;
            case 22050:
                freqIdx = 7;
                break;
            case 16000:
                freqIdx = 8;
                break;
            case 12000:
                freqIdx = 9;
                break;
            case 11025:
                freqIdx = 10;
                break;
            case 8000:
                freqIdx = 11;
                break;
            case 7350:
                freqIdx = 12;
                break;
            default:
                freqIdx = 8;
                break;
        }

        return freqIdx;
    }

}
