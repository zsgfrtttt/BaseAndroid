package com.hydbest.baseandroid.messenger;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import androidx.annotation.Nullable;

/**
 * Created by csz on 2018/6/12.
 */

public class MessageService extends Service{
    private static final int MSG_SUM = 0x110;

    @SuppressLint("HandlerLeak")
    private Messenger mMessenger = new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Message msgToClient =Message.obtain(msg);
            super.handleMessage(msg);
            if (msg.what == MSG_SUM){
                try {
                    Thread.sleep(2000);
                    msgToClient.arg2 = msgToClient.arg1 + msgToClient.arg2;
                    msgToClient.replyTo.send(msgToClient);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    });

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
