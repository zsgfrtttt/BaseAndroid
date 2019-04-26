package com.hydbest.baseandroid.activity.md;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.util.StatusBarUtil;

import java.util.Random;

/**
 * Created by csz on 2018/10/31.
 */

public class MD_StatusBarActivity extends AppCompatActivity {
    int[] color = {R.color.color1, R.color.color2, R.color.color3, R.color.color4, R.color.color5};
    boolean dark = true;

    private LinearLayout mLinearLayout;

    boolean fit = true;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_md_status_bar);
        mLinearLayout = findViewById(R.id.root);
        StatusBarUtil.transparencyBar(this);
     //   StatusBarUtil.setStatusBarColor(this, color[2]);
        StatusBarUtil.setLightStatusBar(this, dark);
    }

    public void changeBgColor(View view) {
        int index = new Random().nextInt(color.length);
        StatusBarUtil.setStatusBarColor(this, color[index]);
    }

    public void changeTextColor(View view) {
        StatusBarUtil.setLightStatusBar(this, dark = (!dark));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void changeFill(View view) {
        //view.setFitsSystemWindows(true);
        fit = !fit;
        //StatusBarUtil.setFitsSystemWindows(this,fit);
        mLinearLayout.setPadding(mLinearLayout.getPaddingLeft(), fit ? getStatusDimensionSize() : 0, mLinearLayout.getPaddingRight(), mLinearLayout.getPaddingBottom());
    }

    private int getStatusDimensionSize() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
