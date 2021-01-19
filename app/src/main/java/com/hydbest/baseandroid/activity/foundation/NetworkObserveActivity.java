package com.hydbest.baseandroid.activity.foundation;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.util.network.NetworkStateManager;
import com.hydbest.baseandroid.util.network.NetworkType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NetworkObserveActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_observe);
        NetworkStateManager.withRegisteNetworkCallback(this);
    }

    public void test(View view) {
        boolean networkAvailable = NetworkStateManager.isNetworkAvailable(this);
        boolean wifiAvailable = NetworkStateManager.isWifiAvailable(this);
        NetworkType networkType = NetworkStateManager.getNetworkType(this);
        Log.i("csz", networkAvailable + "   " + wifiAvailable + "   " + networkType.getDesc());
        try {
            test();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("csz","err   " + e.getMessage());
        }
    }

    public void test() throws Exception {
        PackageManager pm = getPackageManager();
        // Return a List of all packages that are installed on the device.icon
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) // 非系统应用
            {
                //video.like
                //com.wrongchao.v2vpn
                //com.sbds.miaoyin
                if (packageInfo.packageName.equals("com.wrongchao.v2vpn")){
                    FileInputStream fileInputStream = new FileInputStream(packageInfo.applicationInfo.sourceDir);
                    File externalCacheDir = getExternalCacheDir();
                    File file = new File(externalCacheDir,"v2v.apk");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    long copy = FileUtils.copy(fileInputStream, fileOutputStream);
                    Log.i("csz","size    "  + copy);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    fileInputStream.close();
                }else{
                    Log.i("csz","info   " + packageInfo.applicationInfo.sourceDir +"   " +   packageInfo.packageName);
                }

            }
        }
    }


}
