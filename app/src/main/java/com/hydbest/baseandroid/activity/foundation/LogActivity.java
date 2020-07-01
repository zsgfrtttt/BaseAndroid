package com.hydbest.baseandroid.activity.foundation;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hydbest.baseandroid.util.L;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by csz on 2018/7/24.
 */

public class LogActivity extends AppCompatActivity {

    private TextView tv;
    private ImageView iv;

    private boolean lefted;
    private boolean isUse = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.hydbest.baseandroid.R.layout.activity_log);
        L.e("csz");
        L.json("{\"status\":\"0000\",\"code\":\"0000\",\"msg\":null,\"debugMsg\":\"no debug message\",\"data\":{\"id\":3228268,\"userId\":91408147889386123,\"username\":null,\"mobile\":\"MB:6ac7249adacd27d708fdc9442522b017\",\"email\":null,\"password\":\"dc483e80a7a0bd9ef71d8cf973673924\",\"gesturePassword\":null,\"fingerprint\":null,\"status\":1,\"lastActiveGmt\":1532421450420,\"lastLogonAt\":1532341096000,\"failedCount\":0,\"createdAt\":1525835428000,\"src\":0,\"token\":\"1817022193589673\",\"channelCode\":null,\"channel\":\"PP\",\"platformSource\":\"LBD\"}}\n");
    }


    @Override
    protected void onStart() {
        super.onStart();
        //PVSdk.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            View decorView = window.getDecorView();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            decorView.setPadding(decorView.getPaddingLeft(), decorView.getTop() + getStatusBarHeight(this), decorView.getPaddingRight(), decorView.getPaddingBottom());
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            //contentView顶上去，fitsSystemWindows 为true ，会消耗部分间隙
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            decorView.setPadding(decorView.getPaddingLeft(), decorView.getTop() + getStatusBarHeight(this), decorView.getPaddingRight(), decorView.getPaddingBottom());
        }

    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    @Override
    protected void onResume() {
        super.onResume();
        PVSdk.onResume();
    }

    public static class PVSdk {

        public static void onResume() {
            //检测是否在某个方法里面调用
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            boolean result = false;
            for (StackTraceElement stackTraceElement : stackTrace) {
                String methodName = stackTraceElement.getMethodName();
                String className = stackTraceElement.getClassName();
                try {
                    //父类在前
                    boolean assignableFromClass = Activity.class.isAssignableFrom(Class.forName(className));
                    if (assignableFromClass && "onResume".equals(methodName)) {
                        result = true;
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    // ignored
                }
            }
            if (!result) throw new RuntimeException("PVSdk.onResume must in Activity.onResume");
        }
    }

}
