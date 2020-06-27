package com.hydbest.baseandroid.activity.other;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.csz.patch.Ioffer;
import com.hydbest.baseandroid.R;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import dalvik.system.DexClassLoader;

public class HotFixActivity extends AppCompatActivity {

    private Ioffer ioffer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotfix);
    }

    public void toast(View view) {
        File patch = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + "target.jar");
        if (!patch.exists()){
            ioffer = new Ioffer() {
                @Override
                public String offer() {
                    return "so give you a offer";
                }
            };
        }else {
            DexClassLoader loader = new DexClassLoader(patch.getAbsolutePath(),getExternalCacheDir().getAbsolutePath(),null,getClassLoader());
            try {
                Class<?> clz = loader.loadClass("com.csz.patch.OfferImpl");
                ioffer = (Ioffer) clz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(this,ioffer.offer(),Toast.LENGTH_SHORT).show();
    }
}
