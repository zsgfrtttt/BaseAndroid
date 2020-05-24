package com.hydbest.baseandroid.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class SystemBarUtil {

    public static void setStatusBarColor(Activity activity, int color,View view) {
        FrameLayout decorView = (FrameLayout) activity.getWindow().getDecorView();
        FrameLayout content = decorView.findViewById(android.R.id.content);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) content.getLayoutParams();
        int statusBarHeight = getStatusBarHeight(activity.getResources());
        //layoutParams.topMargin += statusBarHeight;
        // content.setLayoutParams(layoutParams);

        view.setPadding(view.getPaddingLeft(),view.getPaddingTop() + statusBarHeight, view.getPaddingRight(),view.getPaddingBottom());

        ColorView colorView = new ColorView(activity);
        colorView.setId(1);
        colorView.setBackgroundColor(color);
        colorView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight));
       // decorView.addView(colorView);
    }

    public static void setAlpha(Activity activity,  int color) {
        FrameLayout decorView = (FrameLayout) activity.getWindow().getDecorView();
        View statueBar = decorView.findViewById(1);
        statueBar.setBackgroundColor(color);
    }

    private static int getStatusBarHeight(Resources res) {
        int result = 0;
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }


    static class ColorView extends View {

        public ColorView(Context context) {
            this(context, null);
        }

        public ColorView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public ColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            setBackgroundColor(Color.argb(0, 0, 0, 0));
        }
    }
}
