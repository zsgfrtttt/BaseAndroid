package com.hydbest.baseandroid.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

/**
 * Created by csz on 2018/7/24.
 */

public class LogMonitor {

    private static LogMonitor sInstance = new LogMonitor();
    private HandlerThread mLogThread = new HandlerThread("log");
    private Handler mIoHandler;
    private static final long TIME_BLOCK = 1000L;

    private LogMonitor() {
        mLogThread.start();
        mIoHandler = new Handler(mLogThread.getLooper());
    }

    private static Runnable mLogRunnable = new Runnable() {
        @Override
        public void run() {
            //把当前阻塞主线程的耗时任务打印出来
            StringBuilder sb = new StringBuilder();
            StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
            for (StackTraceElement s : stackTrace) {
                sb.append(s.toString() + "\n");
            }
            Log.e("csz", sb.toString());
        }
    };

    public static LogMonitor getInstance() {
        return sInstance;
    }

    public boolean isMonitor() {
        // return mIoHandler.hasCallbacks(mLogRunnable);
        return false;
    }

    /**
     * 开启一个打印方法栈的任务，1秒后执行
     */
    public void startMonitor() {
        mIoHandler.postDelayed(mLogRunnable, TIME_BLOCK);
    }

    /**
     * 主线程1秒内完成任务会被移除打印耗时任务
     */
    public void removeMonitor() {
        mIoHandler.removeCallbacks(mLogRunnable);
    }

    public void stop() {
        if (mLogThread.isAlive()) {
            mLogThread.quit();
        }
    }

}
