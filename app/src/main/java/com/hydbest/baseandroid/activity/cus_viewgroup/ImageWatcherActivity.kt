package com.hydbest.baseandroid.activity.cus_viewgroup

import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemChildLongClickListener
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.adapter.ImageWatcherAdapter
import com.hydbest.baseandroid.util.Utils
import com.hydbest.baseandroid.view.viewgroup.imagewatcher.CustomLoadingUIProvider
import com.hydbest.baseandroid.view.viewgroup.imagewatcher.ImageWatcher
import com.hydbest.baseandroid.view.viewgroup.imagewatcher.ImageWatcher.OnStateChangedListener
import com.hydbest.baseandroid.view.viewgroup.imagewatcher.ImageWatcherHelper
import com.hydbest.baseandroid.view.viewgroup.imagewatcher.SimpleLoader
import kotlinx.android.synthetic.main.activity_img_watcher.*

class ImageWatcherActivity : AppCompatActivity() {

    private var mAdapter: ImageWatcherAdapter? = null
    private lateinit var iwHelper: ImageWatcherHelper
    private val datas = arrayOf(R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d)
    private var isTranslucentStatus = false
    private var clickedImage: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        isTranslucentStatus = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
            isTranslucentStatus = true
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_img_watcher)
        initView()
    }

    private fun initView() {
        iwHelper = ImageWatcherHelper.with(this, SimpleLoader()) // 一般来讲， ImageWatcher 需要占据全屏的位置
                .setTranslucentStatus(if (!isTranslucentStatus) Utils.calcStatusBarHeight(this) else 0) // 如果不是透明状态栏，你需要给ImageWatcher标记 一个偏移值，以修正点击ImageView查看的启动动画的Y轴起点的不正确
                .setErrorImageRes(R.mipmap.error_picture) // 配置error图标 如果不介意使用lib自带的图标，并不一定要调用这个API
                .setOnPictureLongPressListener { v, uri, pos -> // 长按图片的回调，你可以显示一个框继续提供一些复制，发送等功能
                    Toast.makeText(v.context.applicationContext, "长按了第" + (pos + 1) + "张图片", Toast.LENGTH_SHORT).show()
                }
                .setOnStateChangedListener(object : OnStateChangedListener {
                    override fun onStateChangeUpdate(imageWatcher: ImageWatcher, clicked: ImageView, position: Int, uri: Uri, animatedValue: Float, actionTag: Int) {
                        Log.e("IW", "onStateChangeUpdate [$position][$uri][$animatedValue][$actionTag]")
                    }

                    override fun onStateChanged(imageWatcher: ImageWatcher, position: Int, uri: Uri, actionTag: Int) {
                        if (actionTag == ImageWatcher.STATE_ENTER_DISPLAYING) {
                            Toast.makeText(applicationContext, "点击了图片 [$position]$uri", Toast.LENGTH_SHORT).show()
                        } else if (actionTag == ImageWatcher.STATE_EXIT_HIDING) {
                            Toast.makeText(applicationContext, "退出了查看大图", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                .setLoadingUIProvider(CustomLoadingUIProvider()) // 自定义LoadingUI
        rv.layoutManager = GridLayoutManager(this, 4)
        //rv.addItemDecoration(new SpaceItemDecoration(this).setSpace(14).setSpaceColor(0xFFECECEC));
        rv.adapter = ImageWatcherAdapter(ImageWatcherAdapter.idArray2UriList(resources, datas)).also { mAdapter = it }
        mAdapter!!.setOnItemChildClickListener(object : OnItemChildClickListener {
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                if (view.id == R.id.iv) {
                    clickedImage = view as ImageView
                    val mapping = SparseArray<ImageView?>() // 这个请自行理解，
                    mapping.put(position, clickedImage)
                    val dataList: List<Uri> = mAdapter!!.data
                    iwHelper.show(clickedImage, mapping, dataList)
                }
            }
        })
        mAdapter!!.setOnItemChildLongClickListener(object : OnItemChildLongClickListener {
            override fun onItemChildLongClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int): Boolean {
                if (view.id == R.id.iv) {
                    AlertDialog.Builder(this@ImageWatcherActivity)
                            .setMessage("are you sure?")
                            .setPositiveButton("yes") { dialog, which -> mAdapter!!.remove(position) }
                            .setNegativeButton("no") { dialog, which -> }.create().show()
                }
                return false
            }
        })
    }

    override fun onBackPressed() {
        if (!iwHelper!!.handleBackPressed()) {
            super.onBackPressed()
        }
    }
}