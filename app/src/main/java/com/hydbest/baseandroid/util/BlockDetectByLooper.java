package com.hydbest.baseandroid.util;

import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by csz on 2018/7/25.
 */

public class BlockDetectByLooper {
    private static final String FIELD_mQueue = "mQueue";
    private static final String METHOD_next = "next";

    public static void start() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Looper looper = Looper.getMainLooper();
                final Looper me = looper;
                final MessageQueue queue;
                try {
                    Field field = Looper.class.getDeclaredField(FIELD_mQueue);
                    field.setAccessible(true);
                    queue = (MessageQueue) field.get(me);

                    Method next = queue.getClass().getDeclaredMethod(METHOD_next);
                    next.setAccessible(true);
                    Binder.clearCallingIdentity();
                    for (;;){
                        Message msg = (Message) next.invoke(queue);
                        if (msg == null){
                            return;
                        }
                        LogMonitor.getInstance().startMonitor();
                        msg.getTarget().dispatchMessage(msg);
                       // msg.recycle();
                        LogMonitor.getInstance().removeMonitor();
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
