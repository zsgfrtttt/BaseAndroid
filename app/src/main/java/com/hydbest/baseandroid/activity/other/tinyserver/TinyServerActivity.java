package com.hydbest.baseandroid.activity.other.tinyserver;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hydbest.baseandroid.R;

import java.io.File;
import java.security.Permission;

public class TinyServerActivity extends AppCompatActivity{
    private SimpleHttpServer server;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiny_server);
        init();
    }

    private void init() {
        WebConfiguration webConfiguration = new WebConfiguration(8088,10);
        server = new SimpleHttpServer(webConfiguration);
        server.registerResourceHandler(new ResourceInAssetsHandler(this));
        server.registerResourceHandler(new UploadImageHandler(){
            @Override
            protected void onImageLoaded(String imgPath) {
                loadImage(imgPath);
            }
        });
    }

    public void startService(View view){
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }else {
            server.startAsync();
            Toast.makeText(this,"服务已经开启！",Toast.LENGTH_LONG).show();
        }
    }

    private void loadImage(final String imgPath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView iv = new ImageView(TinyServerActivity.this);
                iv.setImageURI(Uri.fromFile(new File(imgPath)));
                addContentView(iv,findViewById(R.id.layout).getLayoutParams());
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (server != null) server.stopAsync();
    }
}
