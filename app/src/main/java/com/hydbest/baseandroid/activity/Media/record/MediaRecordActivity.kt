package com.hydbest.baseandroid.activity.Media.record

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.Camera
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hydbest.baseandroid.R
import kotlinx.android.synthetic.main.activity_media_record.*
import java.io.File
import java.io.IOException

class MediaRecordActivity : AppCompatActivity(), SurfaceHolder.Callback, Camera.PreviewCallback {
    private var mMediaRecorder: MediaRecorder? = null
    private var mMediaPlayer: MediaPlayer? = null
    private lateinit var mCamera: Camera
    private var mHolder: SurfaceHolder? = null
    private val mSavePath = Environment.getExternalStorageDirectory().toString() + File.separator + "record.mp4"
    private var surfaceHeight = 0
    private var surfaceWidth = 0
    private val mVoicePath = Environment.getExternalStorageDirectory().toString() + File.separator + "voice"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_record)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(
                    Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }
        mHolder = surface.getHolder()
        mHolder!!.addCallback(this)
        // setType必须设置，要不出错.
        mHolder!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    fun recordVoice(view: View?) {
        try {
            stopRecord()
            stopCamera()
            val file = File(mVoicePath, "vo.m4a")
            file.parentFile.mkdirs()
            if (!file.exists()) {
                file.createNewFile()
            }
            mMediaRecorder = MediaRecorder()
            mMediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mMediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            //所有安卓系统都支持的采样频率
            mMediaRecorder!!.setAudioSamplingRate(44100)
            mMediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mMediaRecorder!!.setAudioEncodingBitRate(96000)
            mMediaRecorder!!.setOutputFile(file.absolutePath)
            mMediaRecorder!!.prepare()
            mMediaRecorder!!.start()
            //获取音量
            mMediaRecorder!!.maxAmplitude
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun record(view: View?) {
        stopRecord()
        try {
            val file = File(mSavePath)
            val fileParent = file.parentFile
            if (!fileParent.exists()) {
                fileParent.mkdirs()
            }
            if (!file.exists()) {
                file.createNewFile()
            }
            //释放掉已经开启的色相头资源，打开新的摄像头绑定录制对象
            stopCamera()
            mMediaRecorder = MediaRecorder() // 创建mediarecorder对象
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
            val parameters = mCamera.getParameters()
            parameters.setRotation(90)
            parameters.setPreviewSize(640, 480)
            parameters.setPictureSize(640, 480)
            mCamera.setParameters(parameters)
            mCamera.setDisplayOrientation(90)
            mCamera.setPreviewDisplay(mHolder)
            mCamera.setPreviewCallback(this)
            mCamera.startPreview()
            mCamera.unlock()
            mMediaRecorder!!.setCamera(mCamera)
            mMediaRecorder!!.reset()

            // 设置录制视频源为Camera(相机)
            mMediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.CAMERA)
            //mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
            mMediaRecorder!!
                    .setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            // 设置录制的视频编码h263 h264
            mMediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
            mMediaRecorder!!.setVideoSize(176 * 2, 144 * 2)
            // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
            mMediaRecorder!!.setVideoFrameRate(60)
            //旋转90度
            mMediaRecorder!!.setOrientationHint(90)
            mMediaRecorder!!.setPreviewDisplay(mHolder!!.surface)
            // 设置视频文件输出的路径
            mMediaRecorder!!.setOutputFile(file.absolutePath)
            // 准备录制
            mMediaRecorder!!.prepare()
            // 开始录制
            mMediaRecorder!!.start()
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
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun stopRecord(view: View?) {
        stopRecord()
    }

    fun play(view: View?) {
        stopPlay()
        //释放掉camera占有的holder对象   否则渲染不出录制的视频
        stopCamera()
        try {
            mMediaPlayer = MediaPlayer()
            mMediaPlayer!!.setDataSource(mSavePath)
            mMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mMediaPlayer!!.setDisplay(mHolder)
            mMediaPlayer!!.prepare()
            mMediaPlayer!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (holder != null) mHolder = holder
        startCamera()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        surfaceWidth = width
        surfaceHeight = height
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopRecord()
        stopCamera()
    }

    /**
     * 打开摄像头
     */
    private fun startCamera() {
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
        mCamera.setDisplayOrientation(90)
        val parameters = mCamera.getParameters()
        parameters.previewFormat = ImageFormat.NV21
        // 这个宽高的设置必须和后面编解码的设置一样，否则不能正常处理
        parameters.setPreviewSize(480, 320)
        try {
            mCamera.setParameters(parameters)
            mCamera.setPreviewDisplay(mHolder)
            mCamera.setPreviewCallback(this)
            mCamera.startPreview()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 关闭摄像头,停止录制
     */
    private fun stopCamera() {
        if (mCamera != null) {
            mCamera!!.setPreviewCallback(null)
            mCamera!!.stopPreview()
            mCamera!!.release()
       //     mCamera = null
        }
    }

    private fun stopRecord() {
        stopPlay()
        try {
            if (mMediaRecorder != null) {
                mMediaRecorder!!.setOnErrorListener(null)
                mMediaRecorder!!.setOnInfoListener(null)
                mMediaRecorder!!.setPreviewDisplay(null)
                mMediaRecorder!!.stop()
                mMediaRecorder!!.release()
                mMediaRecorder = null
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun stopPlay() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    override fun onPreviewFrame(data: ByteArray, camera: Camera) {}

    companion object {
        private const val MAX_AMPLITUDE = 32767
    }
}