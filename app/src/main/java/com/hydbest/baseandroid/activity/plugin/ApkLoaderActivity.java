package com.hydbest.baseandroid.activity.plugin;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hydbest.baseandroid.R;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by csz on 2019/2/1.
 */

public class ApkLoaderActivity extends AppCompatActivity {

    @BindView(R.id.iv)
    ImageView mIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apk_load);
        ButterKnife.bind(this);
    }

    /**
     * 找出所有已安装插件
     *
     * @return
     */
    private List<PluginBean> findAllPlugin() {
        List<PluginBean> list = new ArrayList<>();
        PackageManager pm = getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo packageInfo : installedPackages) {
            String packName = packageInfo.packageName;
            String shareUserId = packageInfo.sharedUserId;
            if (shareUserId != null && shareUserId.equals("share") && !"com.hydbest.baseandroid".equals(packName)) {
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

        return resourceId;
    }

    public void loadPlugin(View view) {
        dynamicLoadApk("com.csz.ni", Environment.getExternalStorageDirectory() + File.separator + "plugin.apk");
    }

    /**
     * 获取插件包名等信息
     */
    private String[] getUninstallApk(String uninstallApkPath) {
        String[] info = new String[2];
        PackageManager pm = getPackageManager();
        PackageInfo packInfo = pm.getPackageArchiveInfo(uninstallApkPath, PackageManager.GET_ACTIVITIES);
        if (packInfo != null) {
            ApplicationInfo applicationInfo = packInfo.applicationInfo;
            String label = pm.getApplicationLabel(applicationInfo).toString();
            String packageName = packInfo.packageName;
            info[0] = label;
            info[1] = packageName;
        }
        return info;
    }

    private Resources getPluginResources(String pluginPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            //getDeclaredMethod
            Method addAssetPath = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, pluginPath);
            Resources origin = getResources();
            Resources plugin = new Resources(assetManager, origin.getDisplayMetrics(), origin.getConfiguration());
            plugin.newTheme().setTo(getTheme());
            return plugin;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载外部资源
     *
     * @param packageName
     * @param pluginPath
     */
    private void dynamicLoadApk(String packageName, String pluginPath) {
        try {
            File dexPath = getDir("dex", Context.MODE_PRIVATE);
            Log.i("csz", "dexPath:" + dexPath);
            DexClassLoader dexClassLoader = new DexClassLoader(pluginPath, dexPath.getAbsolutePath(), null, ClassLoader.getSystemClassLoader());
            Class<?> aClass = dexClassLoader.loadClass(packageName + ".R$mipmap");
            Field one = aClass.getDeclaredField("one");
            int resourceId = one.getInt(null);
            Resources pluginResources = getPluginResources(pluginPath);
            if (pluginResources != null) {
                Drawable bg = pluginResources.getDrawable(resourceId);
                mIv.setImageDrawable(bg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
