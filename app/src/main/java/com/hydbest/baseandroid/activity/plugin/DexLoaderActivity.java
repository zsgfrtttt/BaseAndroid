package com.hydbest.baseandroid.activity.plugin;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.csz.load.IShowToast;
import com.hydbest.baseandroid.R;

import java.io.File;

import dalvik.system.DexClassLoader;

/**
 * Created by csz on 2019/1/31.
 */

public class DexLoaderActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dex_load);
    }

    public void loadDex(View view){
        File dexOutputDir = getDir("dex1", 0);
        Log.i("csz","dexout:"+dexOutputDir.getAbsolutePath());
        String dexPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "output.dex";
        Log.i("csz","dexPath:"+dexPath);
        File file = new File(dexPath);
        Log.i("csz"," exists: " +file.exists() );
        DexClassLoader loader = new DexClassLoader(dexPath,
                dexOutputDir.getAbsolutePath(),
                null, getClassLoader());
        try {
            Class clz = loader.loadClass("com.csz.load.ShowToastImpl");
            IShowToast impl = (IShowToast) clz.newInstance();
            Toast.makeText(this,  impl.getToast(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
}
