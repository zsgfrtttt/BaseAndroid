package com.hydbest.baseandroid.activity.plugin;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hydbest.baseandroid.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalvik.system.PathClassLoader;

/**
 * Created by csz on 2019/2/1.
 */

public class ApkLoaderActivity extends AppCompatActivity {

    @BindView(R.id.iv)
    ImageView iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apk_load);
        ButterKnife.bind(this);
    }

    public void loadApk(View view) {
        if (ActivityCompat.checkSelfPermission(ApkLoaderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(ApkLoaderActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ApkLoaderActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            //findAllPlugin();
            try {
                int id = dynamicLoadResouce("com.ex.pl");
                //int id = dynamicLoadResouce("com.hydbest.baseandroid");
                iv.setImageResource(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<PluginBean> findAllPlugin() {
        List<PluginBean> list = new ArrayList<>();
        PackageManager pm = getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo packageInfo : installedPackages) {
            String packName = packageInfo.packageName;
            String shareUserId = packageInfo.sharedUserId;
            if (shareUserId != null && shareUserId.equals("share.apk") && !"com.hydbest.baseandroid".equals(packName)) {
                String label = pm.getApplicationLabel(packageInfo.applicationInfo).toString();
                Log.i("csz", "label:" + label);
                list.add(new PluginBean(label, packName));
            }
        }
        return list;
    }

    private int dynamicLoadResouce(String packName) throws Exception {
        Context pluginContext = createPackageContext(packName, CONTEXT_IGNORE_SECURITY | CONTEXT_INCLUDE_CODE);
        Log.i("csz", "资源路径：" + pluginContext.getPackageResourcePath());
        PathClassLoader pathClassLoader = new PathClassLoader(pluginContext.getPackageResourcePath(), ClassLoader.getSystemClassLoader());
        Class clazz = Class.forName(packName + ".R$mipmap", true, pathClassLoader);
        Field field = clazz.getDeclaredField("one");
        int resourceId = field.getInt(null);
        Log.i("csz", "资源Id:" + resourceId);
        Drawable drawable = pluginContext.getResources().getDrawable(resourceId);
        iv.setImageDrawable(drawable);
        return resourceId;
    }

    class PluginBean {
        private String label;
        private String packName;

        public PluginBean(String label, String packName) {
            this.label = label;
            this.packName = packName;
        }
    }

}
