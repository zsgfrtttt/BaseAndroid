package com.hydbest.baseandroid.activity.foundation;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.csz.testmodule.L;
import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.util.BlockDetectByLooper;
import com.hydbest.baseandroid.util.BlockDetectByPrinter;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by csz on 2018/7/24.
 * 检测主线程耗时任务
 */

public class BlockDetectActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_detect);
       // BlockDetectByPrinter.start();
       // BlockDetectByChoreographer.start();
        BlockDetectByLooper.start();
        L.getStr();
    }

    public void check(View view) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BlockDetectByPrinter.stop();
    }
}
