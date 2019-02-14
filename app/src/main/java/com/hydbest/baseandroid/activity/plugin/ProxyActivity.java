package com.hydbest.baseandroid.activity.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by csz on 2019/2/11.
 */

public class ProxyActivity extends AppCompatActivity {

    private static final String TAG = "csz";
    public static final String EXTRA_DEX_PATH = "extra.dex.path";
    public static final String EXTRA_CLASS = "extra.class";

    public static final String FROM = "extra.from";
    public static final int FROM_EXTERNAL = 0;
    public static final int FROM_INTERNAL = 1;

    private String mClass;
    private String mDexPath;

    private AssetManager mPluginAssetManager;
    private Resources mPluginResources;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDexPath = getIntent().getStringExtra(EXTRA_DEX_PATH);
        mClass = getIntent().getStringExtra(EXTRA_CLASS);

        mPluginAssetManager = createAssetManager(mDexPath);
        mPluginResources = createResources();

        if (mClass == null) {
            launchTargetActivity();
        } else {
            launchTargetActivity(mClass);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    private void launchTargetActivity() {
        PackageInfo archiveInfo = getPackageManager()
                .getPackageArchiveInfo(mDexPath, PackageManager.GET_ACTIVITIES);
        if (archiveInfo != null && archiveInfo.activities != null && archiveInfo.activities.length > 0) {
            mClass = archiveInfo.activities[0].name;
            launchTargetActivity(mClass);
        }
    }

    private void launchTargetActivity(String aClass) {
        Log.i(TAG, "class:" + aClass);
        File dexOutputDir = this.getDir("dex", Context.MODE_PRIVATE);

        DexClassLoader dexClassLoader = new DexClassLoader(mDexPath,
                dexOutputDir.getAbsolutePath(), null, ClassLoader.getSystemClassLoader());

        try {
            Class<?> loadClass = dexClassLoader.loadClass(aClass);
            Constructor<?> constructor = loadClass.getConstructor(new Class[]{});
            Object instance = constructor.newInstance(new Object[]{});
            Log.i(TAG, "instance:" + instance);

            Method setProxy = loadClass.getMethod("setProxy", Activity.class);
            setProxy.setAccessible(true);
            setProxy.invoke(instance, ProxyActivity.this);

            Method onCreate = loadClass.getDeclaredMethod("onCreate", new Class[]{Bundle.class});
            onCreate.setAccessible(true);
            Bundle bundle = new Bundle();
            bundle.putInt(FROM, FROM_EXTERNAL);
            onCreate.invoke(instance, new Object[]{bundle});
        } catch (Exception e) {
            Log.i(TAG, "e:" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public AssetManager getAssets() {
        return mPluginAssetManager == null ? super.getAssets() : mPluginAssetManager;
    }

    @Override
    public Resources getResources() {
        return mPluginResources == null ? super.getResources() : mPluginResources;
    }

    private AssetManager createAssetManager(String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Resources createResources() {
        Resources superRes = getResources();
        Resources resources = new Resources(getAssets(), superRes.getDisplayMetrics(), superRes.getConfiguration());
        Resources.Theme theme = resources.newTheme();
        theme.setTo(getTheme());
        return resources;
    }
}
