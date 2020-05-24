package com.hydbest.baseandroid.activity.plugin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.csz.load.IShowToast;
import com.hydbest.baseandroid.R;

import java.io.File;

import dalvik.system.DexClassLoader;

/**
 * Created by csz on 2019/1/31.
 * @Function:加载外部dex
 * 参考:dx --dex --output=C:\Users\Administrator\Desktop\classes.dex C:\Users\Administrator\Desktop\output.jar  jar转dex
 *
 */

public class DexLoaderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dex_load);
    }

    public void loadDex(View view) {
        if (ActivityCompat.checkSelfPermission(DexLoaderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(DexLoaderActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DexLoaderActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            File dexOutputDir = getDir("dex1", 0);
            String dexPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "output.dex";
            File file = new File(dexPath);
            DexClassLoader loader = new DexClassLoader(dexPath,
                    dexOutputDir.getAbsolutePath(),
                    null, getClassLoader());
            try {
                Class clz = loader.loadClass("com.csz.load.ShowToastImpl");
                IShowToast impl = (IShowToast) clz.newInstance();
                Toast.makeText(this, impl.getToast(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
