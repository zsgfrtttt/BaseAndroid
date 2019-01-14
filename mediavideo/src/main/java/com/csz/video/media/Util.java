package com.csz.video.media;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

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

}
