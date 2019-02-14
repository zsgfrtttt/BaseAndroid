package com.hydbest.baseandroid.activity.plugin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.activity.Media.MediaExecutorActivity;

/**
 * Created by csz on 2019/2/11.
 */

public class ActivityLoaderActivity extends AppCompatActivity {

    public static final String DEX_PATH = "/mnt/sdcard/plugin.apk";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_act);
    }

    public void loadAct(View view){
        if (ActivityCompat.checkSelfPermission(ActivityLoaderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(ActivityLoaderActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActivityLoaderActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            Intent intent = new Intent(this, ProxyActivity.class);
            intent.setAction("com.csz.ni.activity.VIEW");
            intent.putExtra(ProxyActivity.EXTRA_DEX_PATH, DEX_PATH);
            intent.putExtra(ProxyActivity.EXTRA_CLASS, "com.csz.ni.activity.MainActivity");
            startActivity(intent);
        }
    }
}
