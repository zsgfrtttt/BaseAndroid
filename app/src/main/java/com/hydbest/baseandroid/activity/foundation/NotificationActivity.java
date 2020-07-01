package com.hydbest.baseandroid.activity.foundation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.hydbest.baseandroid.activity.cus_view.AdActivity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class NotificationActivity extends AppCompatActivity{
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final String CHANNEL_NAME = "CHANNEL_NAME";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.hydbest.baseandroid.R.layout.activity_notification);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public Notification.Builder channel(View view){
        Notification.Builder  builder = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("this is a 内容标题")
                .setContentText("今天是星期六，贼几把开心。")
                .setSmallIcon(com.hydbest.baseandroid.R.drawable.a); //必须属性  否则不显示


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.canBypassDnd();//能否绕开请勿打扰模式
            channel.canShowBadge();//应用小红点计数显示

            channel.enableLights(true);//led发光
            channel.setLightColor(Color.GREEN);
            channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);//锁屏显示
            channel.enableVibration(true);//开启震动
            channel.setBypassDnd(true);
            channel.setVibrationPattern(new long[]{100,100,200});

            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID);
        }

        Notification notification = builder.build();
       // notificationManager.notify(0,notification);

        final Notification.Builder reBuilder = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("this is a 内容标题")
                .setContentText("aaaaa")
                .setSmallIcon(com.hydbest.baseandroid.R.drawable.a); //必须属性  否则不显示
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            reBuilder.setChannelId(CHANNEL_ID);
        }
        return reBuilder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void progress(View view){
        final Notification.Builder builder = channel(view).setDefaults(Notification.FLAG_ONLY_ALERT_ONCE);
        notificationManager.notify(1,builder.build());

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while (i<100)
                try {
                    Thread.sleep(300);
                    builder.setProgress(100,i++,false).setDefaults(Notification.FLAG_ONLY_ALERT_ONCE);
                    notificationManager.notify(1,builder.build());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void customLayout(View view){
        final Notification.Builder builder = channel(view);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), com.hydbest.baseandroid.R.layout.layout_notification_vus);
        remoteViews.setTextViewText(com.hydbest.baseandroid.R.id.tv1,"title");
        remoteViews.setTextViewText(com.hydbest.baseandroid.R.id.tv2,"content");

        PendingIntent pendingIntent = PendingIntent.getActivity(this,-1,new Intent(this,AdActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(com.hydbest.baseandroid.R.id.iv,pendingIntent);
        builder.setCustomContentView(remoteViews);
        notificationManager.notify(3,builder.build());
    }
}
