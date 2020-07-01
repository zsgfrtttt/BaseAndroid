package com.hydbest.baseandroid.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.File;

import androidx.core.content.FileProvider;

/**
 * Created by csz on 2018/10/18.
 */

public class ApkExtract {

    public static String extract(Context context) {
        Context applicationContext = context.getApplicationContext();
        ApplicationInfo applicationInfo = applicationContext.getApplicationInfo();
        String apkPath = applicationInfo.sourceDir;
        Log.i("csz","sourceDir:"+apkPath);
        return apkPath;
    }

    public static void install(Context context, String apkPath) {
        final Intent install = new Intent();
        File file = new File(apkPath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context.getApplicationContext(), context.getPackageName()+".android7.fileprovider", file);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setAction(Intent.ACTION_VIEW);
        context.getApplicationContext().startActivity(install);
    }

}
