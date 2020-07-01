package com.hydbest.baseandroid.util;

import android.content.Context;

public class DimenUtil {

    public static float dp2px(Context context,int dp){
        return context.getResources().getDisplayMetrics().density * dp +  0.5f;
    }
}
