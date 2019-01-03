package com.hydbest.baseandroid.activity.foundation;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hydbest.baseandroid.util.L;

/**
 * Created by csz on 2018/7/24.
 */

public class LogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.hydbest.baseandroid.R.layout.activity_log);
        L.e("csz");
        L.json("{\"status\":\"0000\",\"code\":\"0000\",\"msg\":null,\"debugMsg\":\"no debug message\",\"data\":{\"id\":3228268,\"userId\":91408147889386123,\"username\":null,\"mobile\":\"MB:6ac7249adacd27d708fdc9442522b017\",\"email\":null,\"password\":\"dc483e80a7a0bd9ef71d8cf973673924\",\"gesturePassword\":null,\"fingerprint\":null,\"status\":1,\"lastActiveGmt\":1532421450420,\"lastLogonAt\":1532341096000,\"failedCount\":0,\"createdAt\":1525835428000,\"src\":0,\"token\":\"1817022193589673\",\"channelCode\":null,\"channel\":\"PP\",\"platformSource\":\"LBD\"}}\n");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //PVSdk.onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PVSdk.onResume();
    }

    public static class PVSdk {

        public static void onResume() {
            //检测是否在某个方法里面调用
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            boolean result = false;
            for (StackTraceElement stackTraceElement : stackTrace) {
                String methodName = stackTraceElement.getMethodName();
                String className = stackTraceElement.getClassName();
                try {
                    //父类在前
                    boolean assignableFromClass = Activity.class.isAssignableFrom(Class.forName(className));
                    if (assignableFromClass && "onResume".equals(methodName)) {
                        result = true;
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    // ignored
                }
            }
            if (!result)
                throw new RuntimeException("PVSdk.onResume must in Activity.onResume");
        }
    }

}
