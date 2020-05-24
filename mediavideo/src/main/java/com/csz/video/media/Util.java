package com.csz.video.media;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Rect;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;

public class Util {

    public static int getVisiblePercent(View view){
        if (view != null && view.isShown()){
            int screentWidth = view.getContext().getResources().getDisplayMetrics().widthPixels;
            Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);
            double visibleArea =  rect.width() * rect.height();
            double totalArea = view.getWidth() * view.getHeight();
            return (int) (visibleArea / totalArea * 100);
        }
        return -1;
    }

    /**
     * 获取当前用户自动播放的网络设置（4g wifi）判断能否自动播放
     * @param context
     * @return
     */
    public static boolean canAutoPlay(Context context){
        return true;
    }


    /**
     * 切换横竖屏
     * @param context
     * @param orientation
     */
    public static void setRequestedOrientation(Context context, int orientation) {
        if (getAppCompActivity(context) != null) {
            getAppCompActivity(context).setRequestedOrientation(
                    orientation);
        } else {
            scanForActivity(context).setRequestedOrientation(
                    orientation);
        }
    }



    public static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null) return null;
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        } else if (context instanceof ContextThemeWrapper) {
            return getAppCompActivity(((ContextThemeWrapper) context).getBaseContext());
        }
        return null;
    }

    public static Activity scanForActivity(Context context) {
        if (context == null) return null;

        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    @SuppressLint("RestrictedApi")
    public static void showActionBar(Context context) {
        ActionBar ab = getAppCompActivity(context).getSupportActionBar();
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false);
            ab.show();
        }
        scanForActivity(context)
                .getWindow()
                .clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @SuppressLint("RestrictedApi")
    public static void hideActionBar(Context context) {
        ActionBar ab = getAppCompActivity(context).getSupportActionBar();
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false);
            ab.hide();
        }
        scanForActivity(context)
                .getWindow()
                .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
