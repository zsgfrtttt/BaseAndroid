package com.hydbest.baseandroid.util;

import android.os.Looper;
import android.util.Printer;

/**
 * Created by csz on 2018/7/24.
 */

public class BlockDetectByPrinter {
    public static void start() {

        Looper.getMainLooper().setMessageLogging(new Printer() {

            private static final String START = ">>>>> Dispatching";
            private static final String END = "<<<<< Finished";

            @Override
            public void println(String x) {
                if (x.startsWith(START)) {
                    LogMonitor.getInstance().startMonitor();
                }
                if (x.startsWith(END)) {
                    LogMonitor.getInstance().removeMonitor();
                }
            }
        });

    }

    public static void stop() {
        LogMonitor.getInstance().stop();
    }

}
