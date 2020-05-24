package com.hydbest.baseandroid.activity.other;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.natived.BsPatch;
import com.hydbest.baseandroid.util.ApkExtract;

import java.io.File;

/**
 * Created by csz on 2018/10/18.
 * 参考 https://blog.csdn.net/hmg25/article/details/8100896
 *     https://blog.csdn.net/lmj623565791/article/details/52761658
 *     工具（https://download.csdn.net/download/hmg25/4676737）
 */

public class PatchUpdateActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patch_update);
    }

    public void patch(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            ApkExtract.extract(this);
            final File destApk = new File(Environment.getExternalStorageDirectory(), "dest.apk");
            final File patch = new File(Environment.getExternalStorageDirectory(), "patch.patch");

            if (!patch.exists()) return;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //一定要检查文件都存在(源apk和patch)
                    BsPatch.bspatch(ApkExtract.extract(PatchUpdateActivity.this),
                            destApk.getAbsolutePath(),
                            patch.getAbsolutePath());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (destApk.exists())
                                ApkExtract.install(PatchUpdateActivity.this, destApk.getAbsolutePath());
                        }
                    });

                }
            }).start();

        }
    }

    public void toast(View view) {
        Toast.makeText(this, "this is a toast !iiiiiiiiiiiiiiiiiiii", Toast.LENGTH_LONG).show();
    }
}
