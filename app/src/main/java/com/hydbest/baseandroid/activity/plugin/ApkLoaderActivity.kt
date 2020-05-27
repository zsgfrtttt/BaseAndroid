package com.hydbest.baseandroid.activity.plugin

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hydbest.baseandroid.R
import dalvik.system.DexClassLoader
import dalvik.system.PathClassLoader
import kotlinx.android.synthetic.main.activity_apk_load.*
import java.io.File
import java.util.*

/**
 * Created by csz on 2019/2/1.
 */
class ApkLoaderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apk_load)
    }

    /**
     * 找出所有已安装插件
     *
     * @return
     */
    private fun findAllPlugin(): List<PluginBean> {
        val list: MutableList<PluginBean> = ArrayList()
        val pm = packageManager
        val installedPackages = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES)
        for (packageInfo in installedPackages) {
            val packName = packageInfo.packageName
            val shareUserId = packageInfo.sharedUserId
            if (shareUserId != null && shareUserId == "share" && "com.hydbest.baseandroid" != packName) {
                val label = pm.getApplicationLabel(packageInfo.applicationInfo).toString()
                Log.i("csz", "label:$label")
                list.add(PluginBean(label, packName))
            }
        }
        return list
    }

    @Throws(Exception::class)
    private fun dynamicLoadResouce(packName: String): Int {
        val pluginContext = createPackageContext(packName, Context.CONTEXT_IGNORE_SECURITY or Context.CONTEXT_INCLUDE_CODE)
        Log.i("csz", "资源路径：" + pluginContext.packageResourcePath)
        val pathClassLoader = PathClassLoader(pluginContext.packageResourcePath, ClassLoader.getSystemClassLoader())
        val clazz = Class.forName("$packName.R\$mipmap", true, pathClassLoader)
        val field = clazz.getDeclaredField("one")
        val resourceId = field.getInt(null)
        Log.i("csz", "资源Id:$resourceId")
        val drawable = pluginContext.resources.getDrawable(resourceId)
        return resourceId
    }

    fun loadPlugin(view: View?) {
        dynamicLoadApk("com.csz.ni", Environment.getExternalStorageDirectory().toString() + File.separator + "plugin.apk")
    }

    /**
     * 获取插件包名等信息
     */
    private fun getUninstallApk(uninstallApkPath: String): Array<String?> {
        val info = arrayOfNulls<String>(2)
        val pm = packageManager
        val packInfo = pm.getPackageArchiveInfo(uninstallApkPath, PackageManager.GET_ACTIVITIES)
        if (packInfo != null) {
            val applicationInfo = packInfo.applicationInfo
            val label = pm.getApplicationLabel(applicationInfo).toString()
            val packageName = packInfo.packageName
            info[0] = label
            info[1] = packageName
        }
        return info
    }

    private fun getPluginResources(pluginPath: String): Resources? {
        try {
            val assetManager = AssetManager::class.java.newInstance()
            //getDeclaredMethod
            val addAssetPath = assetManager.javaClass.getDeclaredMethod("addAssetPath", String::class.java)
            addAssetPath.invoke(assetManager, pluginPath)
            val origin = resources
            val plugin = Resources(assetManager, origin.displayMetrics, origin.configuration)
            plugin.newTheme().setTo(theme)
            return plugin
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 加载外部资源
     *
     * @param packageName
     * @param pluginPath
     */
    private fun dynamicLoadApk(packageName: String, pluginPath: String) {
        try {
            val dexPath = getDir("dex", Context.MODE_PRIVATE)
            Log.i("csz", "dexPath:$dexPath")
            val dexClassLoader = DexClassLoader(pluginPath, dexPath.absolutePath, null, ClassLoader.getSystemClassLoader())
            val aClass = dexClassLoader.loadClass("$packageName.R\$mipmap")
            val one = aClass.getDeclaredField("one")
            val resourceId = one.getInt(null)
            val pluginResources = getPluginResources(pluginPath)
            if (pluginResources != null) {
                val bg = pluginResources.getDrawable(resourceId)
                iv.setImageDrawable(bg)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    internal inner class PluginBean(private val label: String, private val packName: String)
}