package com.hydbest.baseandroid.activity.Media

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.TextureView.SurfaceTextureListener
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.hydbest.baseandroid.R
import kotlinx.android.synthetic.main.activity_media_camera.*
import java.io.IOException

class MediaCameraActivity : AppCompatActivity() {
    private lateinit var mCamera: Camera
    private lateinit var surfaceTexture: SurfaceTexture
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_camera)
        //在创建surface界面的时候会回调callback
        //surface(null);
        textureTest(null)
    }

    fun surfaceTest(view: View?) {
        surface.visibility = View.VISIBLE
        texture.visibility = View.GONE
        surface.holder.addCallback(object : SurfaceHolder.Callback2 {
            override fun surfaceRedrawNeeded(holder: SurfaceHolder) {}
            override fun surfaceCreated(holder: SurfaceHolder) {
                if (holder == null) return
                if (ActivityCompat.checkSelfPermission(this@MediaCameraActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this@MediaCameraActivity, arrayOf(Manifest.permission.CAMERA), 0)
                } else {
                    openCamerabySurface(holder)
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                if (mCamera != null) {
                    mCamera!!.startPreview()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                if (mCamera != null) {
                    mCamera!!.release()
                }
            }
        })
    }

    fun textureTest(view: View?) {
        texture!!.visibility = View.VISIBLE
        texture!!.invalidate()
        surface!!.visibility = View.GONE
        if (texture!!.isAvailable) {
            if (texture == null) return
            if (ActivityCompat.checkSelfPermission(this@MediaCameraActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@MediaCameraActivity, arrayOf(Manifest.permission.CAMERA), 0)
            } else {
                openCamerabyTexture(surfaceTexture)
            }
            return
        }
        texture!!.surfaceTextureListener = object : SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                if (surface == null) return
                surfaceTexture = surface
                if (ActivityCompat.checkSelfPermission(this@MediaCameraActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this@MediaCameraActivity, arrayOf(Manifest.permission.CAMERA), 0)
                } else {
                    openCamerabyTexture(surface)
                }
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}
            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                if (mCamera != null) mCamera!!.release()
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
        }
    }

    private fun openCamerabySurface(holder: SurfaceHolder) {
        if (mCamera != null) mCamera!!.release()
        mCamera = Camera.open()
        if (mCamera == null) {
            val cametacount = Camera.getNumberOfCameras()
            mCamera = Camera.open(cametacount - 1)
        }
        mCamera.setDisplayOrientation(90)
        try {
            mCamera.setPreviewDisplay(holder)
            mCamera.startPreview()
            //获取回调数据
            val parameters = mCamera.getParameters()
            parameters.previewFormat = ImageFormat.NV21
            mCamera.setParameters(parameters)
            mCamera.setPreviewCallback(Camera.PreviewCallback { data, camera -> })
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun openCamerabyTexture(texture: SurfaceTexture) {
        if (mCamera != null) mCamera!!.release()
        mCamera = Camera.open()
        if (mCamera == null) {
            val cametacount = Camera.getNumberOfCameras()
            mCamera = Camera.open(cametacount - 1)
        }
        mCamera.setDisplayOrientation(90)
        try {
            mCamera.setPreviewTexture(texture)
            mCamera.startPreview()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}