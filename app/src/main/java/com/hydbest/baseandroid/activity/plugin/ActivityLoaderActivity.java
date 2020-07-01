package com.hydbest.baseandroid.activity.plugin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.activity.cus_view.AdActivity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by csz on 2019/2/11.
 */

public class ActivityLoaderActivity extends AppCompatActivity {

    public static final String DEX_PATH = "/mnt/sdcard/plugin.apk";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_act);
        Log.i("csz", "ActivityLoaderActivity:" + (getClassLoader() instanceof DexClassLoader) + " or  " + (getClassLoader() instanceof PathClassLoader));
    }

    public void loadAct(View view) {
        if (ActivityCompat.checkSelfPermission(ActivityLoaderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(ActivityLoaderActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActivityLoaderActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            DexClassLoader loader = dynamicLoadApk();
            combineElement(loader);
            Class clz = createActivityClass(getClassLoader());
            //invoke(loader);
            Intent intent = new Intent(this, clz);
            startActivity(intent);
        }
    }

    public void loadAct2(View view) {
        if (ActivityCompat.checkSelfPermission(ActivityLoaderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(ActivityLoaderActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActivityLoaderActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            DexClassLoader loader = dynamicLoadApk();
            Class clz = createActivityClass(getClassLoader());
            invoke(loader);
            Intent intent = new Intent(this, clz);
            startActivity(intent);
        }
    }

    private Class<?> createActivityClass(ClassLoader loader) {
        Class<?> clazz = null;
        try {
            clazz = loader.loadClass("com.pl.one.ShadowActivity");
            Method setLayoutId = clazz.getMethod("setLayoutId", int.class);
            //这种方式用的是宿主的ClassLoader 只能用宿主的资源 //activity_main
            setLayoutId.invoke(null, 0x7f0b0038);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return clazz;
    }

    /**
     * 加载外部资源
     */
    private DexClassLoader dynamicLoadApk() {
        try {
            File dexPath = getDir("dex", Context.MODE_PRIVATE);
            Log.i("csz", "dexPath:" + dexPath);
            DexClassLoader dexClassLoader = new DexClassLoader(DEX_PATH, dexPath.getAbsolutePath(), null, ClassLoader.getSystemClassLoader());
            return dexClassLoader;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void invoke(DexClassLoader dexClassLoader) {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = activityThread.getMethod("currentActivityThread");
            Object instance = currentActivityThread.invoke(null, new Object[]{});

            String packageName = this.getPackageName();
            Field mPackages = activityThread.getDeclaredField("mPackages");
            mPackages.setAccessible(true);
            ArrayMap arrayMap = (ArrayMap) mPackages.get(instance);
            WeakReference o = (WeakReference) arrayMap.get(packageName);
            Object loadedApk = o.get();

            Class<?> loadedApkClass = loadedApk.getClass();
            Field mClassLoader = loadedApkClass.getDeclaredField("mClassLoader");
            mClassLoader.setAccessible(true);
            mClassLoader.set(loadedApk, dexClassLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void adActivity(View view){
        startActivity(new Intent(this, AdActivity.class));
    }

    /**
     * 合并dex(不包含资源)
     * @param dexClassLoader
     */
    private void combineElement(DexClassLoader dexClassLoader){
        try {
            Class<?> baseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathList = baseDexClassLoader.getDeclaredField("pathList");

            BaseDexClassLoader pathClassLoader = (BaseDexClassLoader) getClassLoader();
            pathList.setAccessible(true);
            Object pathListObj = pathList.get(pathClassLoader);
            Field dexElements = pathListObj.getClass().getDeclaredField("dexElements");
            dexElements.setAccessible(true);
            Object dexElementsObj = dexElements.get(pathListObj);

            Object pathListObj1 = pathList.get(dexClassLoader);
            Field dexElements1 = pathListObj1.getClass().getDeclaredField("dexElements");
            dexElements1.setAccessible(true);
            Object dexElementsObj1 = dexElements1.get(pathListObj1);
            Object combineArray = combineArray(dexElementsObj, dexElementsObj1);

            dexElements.set(pathListObj,combineArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 合并数组
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

}
