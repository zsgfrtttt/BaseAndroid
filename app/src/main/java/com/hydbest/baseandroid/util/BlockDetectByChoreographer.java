package com.hydbest.baseandroid.util;

import android.os.Build;
import androidx.annotation.RequiresApi;
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
