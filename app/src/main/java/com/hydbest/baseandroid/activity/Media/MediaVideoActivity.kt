package com.hydbest.baseandroid.activity.Media

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.hydbest.baseandroid.R
import kotlinx.android.synthetic.main.activity_media_video.*
import java.io.File

class MediaVideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_video)
    }

    fun play(view: View?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        } else {
            video_view!!.setVideoPath(Environment.getExternalStorageDirectory().absolutePath + File.separator + "好想你.mp4") // 香港卫视地址
            video_view!!.start()
            val mediaController = MediaController(this)
            video_view!!.setMediaController(mediaController)
            mediaController.setMediaPlayer(video_view)
        }
    }

    fun stop(view: View?) {
        if (video_view!!.isPlaying) {
            video_view!!.pause()
        }
    }
}