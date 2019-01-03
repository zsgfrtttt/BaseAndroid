package com.hydbest.baseandroid.activity.Media;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.hydbest.baseandroid.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by csz on 2018/9/13.
 */

public class MediaDrawImageActivity extends AppCompatActivity {

    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.surface)
    SurfaceView surfaceView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_draw_image);
        ButterKnife.bind(this);
    }

    public void drawByFatory(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        } else {
            iv.setVisibility(View.VISIBLE);
            surfaceView.setVisibility(View.GONE);
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
            iv.setImageBitmap(bitmap);
        }
    }

    public void drawBySurface(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        } else {
            iv.setVisibility(View.GONE);
            surfaceView.setVisibility(View.VISIBLE);
            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback2() {
                @Override
                public void surfaceRedrawNeeded(SurfaceHolder holder) {


                }

                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    if (holder == null) return;
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
                    //paint.setStyle(Paint.Style.STROKE);
                    Canvas canvas = holder.lockCanvas();
                    bitmap = Bitmap.createScaledBitmap(bitmap,surfaceView.getWidth(),surfaceView.getHeight(),false);
                    canvas.drawBitmap(bitmap, 0, 0, paint);
                    holder.unlockCanvasAndPost(canvas);
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });
        }
    }
}
