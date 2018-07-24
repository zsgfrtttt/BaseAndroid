package com.hydbest.baseandroid.util;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Choreographer;

/**
 * Created by csz on 2018/7/24.
 */

public class BlockDetectByChoreographer {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void start() {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                Log.i("csz","doFrame:"+System.currentTimeMillis());
                if (LogMonitor.getInstance().isMonitor())
                {
                    LogMonitor.getInstance().removeMonitor();
                }
                LogMonitor.getInstance().startMonitor();
                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }
}
