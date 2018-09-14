package com.hydbest.baseandroid.activity.Media;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;

import com.hydbest.baseandroid.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaCameraActivity extends AppCompatActivity {

    @BindView(R.id.surface)
    SurfaceView surfaceView;
    @BindView(R.id.texture)
    TextureView textureView;

    private Camera mCamera;
    private SurfaceTexture texture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_camera);
        ButterKnife.bind(this);
        //在创建surface界面的时候会回调callback
        //surface(null);
        texture(null);
    }

    public void surface(View view) {
        surfaceView.setVisibility(View.VISIBLE);
        textureView.setVisibility(View.GONE);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback2() {
            @Override
            public void surfaceRedrawNeeded(SurfaceHolder holder) {

            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (holder == null) return;
                if (ActivityCompat.checkSelfPermission(MediaCameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MediaCameraActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                } else {
                    openCamerabySurface(holder);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (mCamera != null) {
                    mCamera.startPreview();
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCamera != null) {
                    mCamera.release();
                }
            }
        });
    }

    public void texture(View view) {
        textureView.setVisibility(View.VISIBLE);
        textureView.invalidate();
        surfaceView.setVisibility(View.GONE);
        if (textureView.isAvailable()) {
            if (texture == null) return;
            if (ActivityCompat.checkSelfPermission(MediaCameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MediaCameraActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
            } else {
                openCamerabyTexture(texture);
            }
            return;
        }
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                if (surface == null) return;
                texture = surface;
                if (ActivityCompat.checkSelfPermission(MediaCameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MediaCameraActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                } else {
                    openCamerabyTexture(surface);
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                if (mCamera != null) mCamera.release();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }

    private void openCamerabySurface(SurfaceHolder holder) {
        if (mCamera != null) mCamera.release();
        mCamera = Camera.open();

        if (mCamera == null) {
            int cametacount = Camera.getNumberOfCameras();
            mCamera = Camera.open(cametacount - 1);
        }

        mCamera.setDisplayOrientation(90);
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            //获取回调数据
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewFormat(ImageFormat.NV21);
            mCamera.setParameters(parameters);
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openCamerabyTexture(SurfaceTexture texture) {
        if (mCamera != null) mCamera.release();
        mCamera = Camera.open();

        if (mCamera == null) {
            int cametacount = Camera.getNumberOfCameras();
            mCamera = Camera.open(cametacount - 1);
        }

        mCamera.setDisplayOrientation(90);
        try {
            mCamera.setPreviewTexture(texture);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
