package com.hydbest.baseandroid.activity.Media.record;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.hydbest.baseandroid.R;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaRecordActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    @BindView(R.id.surface)
    SurfaceView surfaceView;

    private MediaRecorder mMediaRecorder;
    private MediaPlayer mMediaPlayer;
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private String mSavePath = Environment.getExternalStorageDirectory() + File.separator + "record.mp4";
    private int surfaceHeight;
    private int surfaceWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_record);
        ButterKnife.bind(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }

        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);
        // setType必须设置，要不出错.
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void record(View view) {
        stopRecord();
        try {
            File file = new File(mSavePath);
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            //释放掉已经开启的色相头资源，打开新的摄像头绑定录制对象
            stopCamera();
            mMediaRecorder = new MediaRecorder();// 创建mediarecorder对象
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setRotation(90);
            parameters.setPreviewSize(640, 480);
            parameters.setPictureSize(640, 480);
            mCamera.setParameters(parameters);
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);
            mMediaRecorder.reset();

            // 设置录制视频源为Camera(相机)
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            //mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
            mMediaRecorder
                    .setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            // 设置录制的视频编码h263 h264
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
            mMediaRecorder.setVideoSize(176 * 2, 144 * 2);
            // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
            mMediaRecorder.setVideoFrameRate(60);
            //旋转90度
            mMediaRecorder.setOrientationHint(90);
            mMediaRecorder.setPreviewDisplay(mHolder.getSurface());
            // 设置视频文件输出的路径
            mMediaRecorder.setOutputFile(file.getAbsolutePath());
            // 准备录制
            mMediaRecorder.prepare();
            // 开始录制
            mMediaRecorder.start();
            /*mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setCamera(mCamera);
            // 这两项需要放在setOutputFormat之前
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            // Set output file format
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            // 这两项需要放在setOutputFormat之后
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
            mMediaRecorder.setVideoSize(176, 144);
            //60fps高画质
            mMediaRecorder.setVideoFrameRate(20);
            //3m/s高音质
            mMediaRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);
            mMediaRecorder.setOrientationHint(90);
            //设置记录会话的最大持续时间（毫秒）
            mMediaRecorder.setMaxDuration(10 * 1000);
            mMediaRecorder.setPreviewDisplay(mHolder.getSurface());

            mMediaRecorder.setOutputFile(file.getAbsolutePath());
            mMediaRecorder.prepare();
            mMediaRecorder.start();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecord(View view) {
        stopRecord();
    }

    public void play(View view) {
        stopPlay();
        //释放掉camera占有的holder对象   否则渲染不出录制的视频
        stopCamera();
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(mSavePath);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDisplay(mHolder);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder != null) this.mHolder = holder;
        startCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        surfaceWidth = width;
        surfaceHeight = height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopRecord();
        stopCamera();
    }

    /**
     * 打开摄像头
     */
    private void startCamera() {
        mCamera = android.hardware.Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        mCamera.setDisplayOrientation(90);
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewFormat(ImageFormat.NV21);
        // 这个宽高的设置必须和后面编解码的设置一样，否则不能正常处理
        parameters.setPreviewSize(480, 320);

        try {
            mCamera.setParameters(parameters);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭摄像头,停止录制
     */
    private void stopCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private void stopRecord() {
        stopPlay();
        try {
            if (mMediaRecorder != null) {
                mMediaRecorder.setOnErrorListener(null);
                mMediaRecorder.setOnInfoListener(null);
                mMediaRecorder.setPreviewDisplay(null);
                mMediaRecorder.stop();
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void stopPlay() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }
}
