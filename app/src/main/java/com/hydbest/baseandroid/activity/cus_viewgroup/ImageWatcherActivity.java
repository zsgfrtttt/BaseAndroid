package com.hydbest.baseandroid.activity.cus_viewgroup;

import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.adapter.ImageWatcherAdapter;
import com.hydbest.baseandroid.util.Utils;
import com.hydbest.baseandroid.view.viewgroup.imagewatcher.CustomLoadingUIProvider;
import com.hydbest.baseandroid.view.viewgroup.imagewatcher.ImageWatcher;
import com.hydbest.baseandroid.view.viewgroup.imagewatcher.ImageWatcherHelper;
import com.hydbest.baseandroid.view.viewgroup.imagewatcher.SimpleLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageWatcherActivity extends AppCompatActivity{

    @BindView(R.id.rv)
    RecyclerView rv;

    private ImageWatcherAdapter mAdapter;

    private ImageWatcherHelper iwHelper;

    private Integer[] datas = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d};
    private boolean isTranslucentStatus;
    private ImageView clickedImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isTranslucentStatus = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
            isTranslucentStatus = true;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_watcher);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        iwHelper = ImageWatcherHelper.with(this, new SimpleLoader()) // 一般来讲， ImageWatcher 需要占据全屏的位置
                .setTranslucentStatus(!isTranslucentStatus ? Utils.calcStatusBarHeight(this) : 0) // 如果不是透明状态栏，你需要给ImageWatcher标记 一个偏移值，以修正点击ImageView查看的启动动画的Y轴起点的不正确
                .setErrorImageRes(R.mipmap.error_picture) // 配置error图标 如果不介意使用lib自带的图标，并不一定要调用这个API
                .setOnPictureLongPressListener(new ImageWatcher.OnPictureLongPressListener() {
                    @Override
                    public void onPictureLongPress(ImageView v, Uri uri, int pos) {
                        // 长按图片的回调，你可以显示一个框继续提供一些复制，发送等功能
                        Toast.makeText(v.getContext().getApplicationContext(), "长按了第" + (pos + 1) + "张图片", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnStateChangedListener(new ImageWatcher.OnStateChangedListener() {
                    @Override
                    public void onStateChangeUpdate(ImageWatcher imageWatcher, ImageView clicked, int position, Uri uri, float animatedValue, int actionTag) {
                        Log.e("IW", "onStateChangeUpdate [" + position + "][" + uri + "][" + animatedValue + "][" + actionTag + "]");
                    }

                    @Override
                    public void onStateChanged(ImageWatcher imageWatcher, int position, Uri uri, int actionTag) {
                        if (actionTag == ImageWatcher.STATE_ENTER_DISPLAYING) {
                            Toast.makeText(getApplicationContext(), "点击了图片 [" + position + "]" + uri + "", Toast.LENGTH_SHORT).show();
                        } else if (actionTag == ImageWatcher.STATE_EXIT_HIDING) {
                            Toast.makeText(getApplicationContext(), "退出了查看大图", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setLoadingUIProvider(new CustomLoadingUIProvider()); // 自定义LoadingUI

        rv.setLayoutManager(new GridLayoutManager(this,4));
        //rv.addItemDecoration(new SpaceItemDecoration(this).setSpace(14).setSpaceColor(0xFFECECEC));
        rv.setAdapter(mAdapter = new ImageWatcherAdapter(ImageWatcherAdapter.idArray2UriList(getResources(),datas)));
        mAdapter.setOnItemChildClickListener(new com.chad.library.adapter.base.listener.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId()== R.id.iv){
                    clickedImage = (ImageView) view;
                    SparseArray<ImageView> mapping = new SparseArray<>(); // 这个请自行理解，
                    mapping.put(position, clickedImage);
                    List<Uri> dataList = mAdapter.getData();

                    iwHelper.show(clickedImage, mapping, dataList);
                }
            }
        });
        mAdapter.setOnItemChildLongClickListener(new com.chad.library.adapter.base.listener.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, final int position) {
                if (view.getId()== R.id.iv){
                    new AlertDialog.Builder(ImageWatcherActivity.this)
                            .setMessage("are you sure?")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAdapter.remove(position);
                                }
                            })
                            .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!iwHelper.handleBackPressed()) {
            super.onBackPressed();
        }
    }
}
