package com.hydbest.baseandroid.util;

import android.app.Activity;
import android.graphics.Interpolator;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.hydbest.baseandroid.activity.Media.IjkPlayerActivity;
import com.hydbest.baseandroid.activity.Media.MediaAudioRecordActivity;
import com.hydbest.baseandroid.activity.Media.MediaAudioTrackActivity;
import com.hydbest.baseandroid.activity.Media.MediaCameraActivity;
import com.hydbest.baseandroid.activity.Media.MediaCusVideoActivity;
import com.hydbest.baseandroid.activity.Media.MediaDrawImageActivity;
import com.hydbest.baseandroid.activity.Media.MediaExecutorActivity;
import com.hydbest.baseandroid.activity.Media.MediaVideoActivity;
import com.hydbest.baseandroid.activity.Media.MediaVoiceActivity;
import com.hydbest.baseandroid.activity.Media.openGL_ES.GLSurfaceActivity;
import com.hydbest.baseandroid.activity.Media.openGL_ES.GL_DefineShapeActivity;
import com.hydbest.baseandroid.activity.Media.record.MediaRecordActivity;
import com.hydbest.baseandroid.activity.anim.drawable.DrawableActivity;
import com.hydbest.baseandroid.activity.anim.fragment.FragmentTransitionActivity;
import com.hydbest.baseandroid.activity.anim.interpolator.InterpolatorActivity;
import com.hydbest.baseandroid.activity.anim.motion.MotionActivity;
import com.hydbest.baseandroid.activity.anim.revealeffect.RevealEffectActivity;
import com.hydbest.baseandroid.activity.anim.scene.SceneTransitionAnimationActivity;
import com.hydbest.baseandroid.activity.anim.transition.BasicTransitionAnimationActivity;
import com.hydbest.baseandroid.activity.anim.transition.CustomTransitionActivity;
import com.hydbest.baseandroid.activity.anim.unsplash.UnSplashActivity;
import com.hydbest.baseandroid.activity.concurrent.CountDownLatchActivity;
import com.hydbest.baseandroid.activity.concurrent.ExecuteServiceActivity;
import com.hydbest.baseandroid.activity.concurrent.FutureActivity;
import com.hydbest.baseandroid.activity.concurrent.ReentrantLockActivity;
import com.hydbest.baseandroid.activity.concurrent.ThreadConcurrentActivity;
import com.hydbest.baseandroid.activity.concurrent.ThreadLocalActivity;
import com.hydbest.baseandroid.activity.cus_view.AdActivity;
import com.hydbest.baseandroid.activity.cus_view.AliPayLoadActivity;
import com.hydbest.baseandroid.activity.cus_view.LargeImageActivity;
import com.hydbest.baseandroid.activity.cus_view.MultiTouchActivity;
import com.hydbest.baseandroid.activity.cus_view.MusicDropActivity;
import com.hydbest.baseandroid.activity.cus_view.RippleActivity;
import com.hydbest.baseandroid.activity.cus_view.TextActivity;
import com.hydbest.baseandroid.activity.cus_view.TextAreaActivity;
import com.hydbest.baseandroid.activity.cus_view.ZoomImageActivity;
import com.hydbest.baseandroid.activity.cus_view.shadow.sample.ShadowActivity;
import com.hydbest.baseandroid.activity.cus_viewgroup.CalendarActivity;
import com.hydbest.baseandroid.activity.cus_viewgroup.ConstraintActivity;
import com.hydbest.baseandroid.activity.cus_viewgroup.ImageWatcherActivity;
import com.hydbest.baseandroid.activity.cus_viewgroup.KeyboardActivity;
import com.hydbest.baseandroid.activity.cus_viewgroup.LeftDrawerLayoutActivity;
import com.hydbest.baseandroid.activity.cus_viewgroup.NavigationActivity;
import com.hydbest.baseandroid.activity.cus_viewgroup.NestScrollActivity;
import com.hydbest.baseandroid.activity.cus_viewgroup.PagerIndicatorActivity;
import com.hydbest.baseandroid.activity.cus_viewgroup.ViewpageActivity;
import com.hydbest.baseandroid.activity.event.EventRegisterActivity;
import com.hydbest.baseandroid.activity.foundation.ArrayActivity;
import com.hydbest.baseandroid.activity.foundation.AtActivity;
import com.hydbest.baseandroid.activity.foundation.BlockDetectActivity;
import com.hydbest.baseandroid.activity.foundation.ContextMenuActivity;
import com.hydbest.baseandroid.activity.foundation.FileProviderActivity;
import com.hydbest.baseandroid.activity.foundation.GifActivity;
import com.hydbest.baseandroid.activity.foundation.LogActivity;
import com.hydbest.baseandroid.activity.foundation.NetworkObserveActivity;
import com.hydbest.baseandroid.activity.foundation.NotificationActivity;
import com.hydbest.baseandroid.activity.foundation.RuntimePermissonActivity;
import com.hydbest.baseandroid.activity.foundation.VlayoutActivity;
import com.hydbest.baseandroid.activity.foundation.ZxingActivity;
import com.hydbest.baseandroid.activity.foundation.transition.ListTransitionActivity;
import com.hydbest.baseandroid.activity.jetpack.DataBindingActivity;
import com.hydbest.baseandroid.activity.jetpack.JetpackNavigationActivity;
import com.hydbest.baseandroid.activity.jetpack.KtActivity;
import com.hydbest.baseandroid.activity.jetpack.LifeCycleActivity;
import com.hydbest.baseandroid.activity.jetpack.LiveViewModelActivity;
import com.hydbest.baseandroid.activity.jetpack.PagingActivity;
import com.hydbest.baseandroid.activity.jetpack.RoomActivity;
import com.hydbest.baseandroid.activity.jetpack.ViewBindingActivity;
import com.hydbest.baseandroid.activity.jetpack.WorkerActivity;
import com.hydbest.baseandroid.activity.jetpack.paging.itemkey.BoundaryActivity;
import com.hydbest.baseandroid.activity.jetpack.paging.itemkey.ItemKeyDataSourceActivity;
import com.hydbest.baseandroid.activity.jetpack.paging.pagekey.PageKeyDataSourceActivity;
import com.hydbest.baseandroid.activity.jetpack.paging.positional.PositionalDataSourceActivity;
import com.hydbest.baseandroid.activity.md.MD_ButtonActivity;
import com.hydbest.baseandroid.activity.md.MD_StatusBarActivity;
import com.hydbest.baseandroid.activity.md.MatraialShapeDrawableActivity;
import com.hydbest.baseandroid.activity.md.MotionLayoutActivity;
import com.hydbest.baseandroid.activity.md.RecycleViewAnimActivity;
import com.hydbest.baseandroid.activity.md.RecycleViewDecorateActivity;
import com.hydbest.baseandroid.activity.md.SvgActivity;
import com.hydbest.baseandroid.activity.md.TranslucentBarActivity;
import com.hydbest.baseandroid.activity.md.UiflagActivity;
import com.hydbest.baseandroid.activity.md.ViewOutlineActivity;
import com.hydbest.baseandroid.activity.other.AndroidAdvanceActivity;
import com.hydbest.baseandroid.activity.other.HotFixActivity;
import com.hydbest.baseandroid.activity.other.PatchUpdateActivity;
import com.hydbest.baseandroid.activity.other.syncAdapter.AnnotationProcessActivity;
import com.hydbest.baseandroid.activity.other.syncAdapter.SyncAdapterActivity;
import com.hydbest.baseandroid.activity.other.tinyserver.TinyServerActivity;
import com.hydbest.baseandroid.activity.plugin.ActivityLoaderActivity;
import com.hydbest.baseandroid.activity.plugin.ApkLoaderActivity;
import com.hydbest.baseandroid.activity.plugin.ClassLoaderActivity;
import com.hydbest.baseandroid.activity.plugin.DexLoaderActivity;
import com.hydbest.baseandroid.aidl.BookActivity;
import com.hydbest.baseandroid.entity.Fragmentation;
import com.hydbest.baseandroid.entity.Level0Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csz on 2018/9/13.
 * 数据生成
 */

public class DataGeneration {

    public static List<BaseNode> getItems() {
        List list = new ArrayList();

        Level0Item item0 = new Level0Item("android基础API", "");
        Fragmentation subItem0_0 = new Fragmentation("运行时权限", RuntimePermissonActivity.class);
        Fragmentation subItem0_1 = new Fragmentation("Log库原理", LogActivity.class);
        Fragmentation subItem0_2 = new Fragmentation("FileProvider", FileProviderActivity.class);
        Fragmentation subItem0_3 = new Fragmentation("ArrayMap和SparseArray的使用", ArrayActivity.class);
        Fragmentation subItem0_4 = new Fragmentation("notification 8.0", NotificationActivity.class);
        Fragmentation subItem0_5 = new Fragmentation("Context menu", ContextMenuActivity.class);
        Fragmentation subItem0_6 = new Fragmentation("列表转场", ListTransitionActivity.class);
        Fragmentation subItem0_7 = new Fragmentation("gif播放及监听", GifActivity.class);
        Fragmentation subItem0_8 = new Fragmentation("aidl使用规范", BookActivity.class);
        Fragmentation subItem0_9 = new Fragmentation("如何优雅的@人", AtActivity.class);
        Fragmentation subItem0_10 = new Fragmentation("zxing扫描", ZxingActivity.class);
        Fragmentation subItem0_11 = new Fragmentation("图片压缩", ZoomImageActivity.class);
        Fragmentation subItem0_12 = new Fragmentation("VLayout使用", VlayoutActivity.class);
        Fragmentation subItem0_13 = new Fragmentation("网络监听器", NetworkObserveActivity.class);
        item0.addSubItem(subItem0_0);
        item0.addSubItem(subItem0_1);
        item0.addSubItem(subItem0_2);
        item0.addSubItem(subItem0_3);
        item0.addSubItem(subItem0_4);
        item0.addSubItem(subItem0_5);
        item0.addSubItem(subItem0_6);
        item0.addSubItem(subItem0_7);
        item0.addSubItem(subItem0_8);
        item0.addSubItem(subItem0_9);
        item0.addSubItem(subItem0_10);
        item0.addSubItem(subItem0_11);
        item0.addSubItem(subItem0_12);
        item0.addSubItem(subItem0_13);

        Level0Item item1 = new Level0Item("android自定义View", "");
        Fragmentation subItem1_0 = new Fragmentation("仿知乎广告", AdActivity.class);
        Fragmentation subItem1_1 = new Fragmentation("加载查看超大图片", LargeImageActivity.class);
        Fragmentation subItem1_2 = new Fragmentation("多触点的View", MultiTouchActivity.class);
        Fragmentation subItem1_3 = new Fragmentation("长文对齐TextView", TextAreaActivity.class);
        Fragmentation subItem1_4 = new Fragmentation("阿里加载view", AliPayLoadActivity.class);
        Fragmentation subItem1_5 = new Fragmentation("阴影ImageView", ShadowActivity.class);
        Fragmentation subItem1_6 = new Fragmentation("背景渐变效果", MusicDropActivity.class);
        Fragmentation subItem1_7 = new Fragmentation("水波纹扩散", RippleActivity.class);
        Fragmentation subItem1_8 = new Fragmentation("文字的绘制", TextActivity.class);
        item1.addSubItem(subItem1_0);
        item1.addSubItem(subItem1_1);
        item1.addSubItem(subItem1_2);
        item1.addSubItem(subItem1_3);
        item1.addSubItem(subItem1_4);
        item1.addSubItem(subItem1_5);
        item1.addSubItem(subItem1_6);
        item1.addSubItem(subItem1_7);
        item1.addSubItem(subItem1_8);

        Level0Item item2 = new Level0Item("android自定义ViewGroup", "");
        Fragmentation subItem2_0 = new Fragmentation("constrainLayout", ConstraintActivity.class);
        Fragmentation subItem2_1 = new Fragmentation("仿微信朋友圈图片查看", ImageWatcherActivity.class);
        Fragmentation subItem2_2 = new Fragmentation("LeftDrawerLayout", LeftDrawerLayoutActivity.class);
        Fragmentation subItem2_3 = new Fragmentation("NavigationView", NavigationActivity.class);
        Fragmentation subItem2_4 = new Fragmentation("嵌套滚动", NestScrollActivity.class);
        Fragmentation subItem2_5 = new Fragmentation("Viewpager transformer", ViewpageActivity.class);
        Fragmentation subItem2_6 = new Fragmentation("自定义日历", CalendarActivity.class);
        Fragmentation subItem2_7 = new Fragmentation("自定义键盘", KeyboardActivity.class);
        Fragmentation subItem2_8 = new Fragmentation("自定义ViewPagerIndicator", PagerIndicatorActivity.class);
        item2.addSubItem(subItem2_0);
        item2.addSubItem(subItem2_1);
        item2.addSubItem(subItem2_2);
        item2.addSubItem(subItem2_3);
        item2.addSubItem(subItem2_4);
        item2.addSubItem(subItem2_5);
        item2.addSubItem(subItem2_6);
        item2.addSubItem(subItem2_7);
        item2.addSubItem(subItem2_8);

        Level0Item item3 = new Level0Item("android性能优化", "");
        Fragmentation subItem3_0 = new Fragmentation("主线程阻塞检测", BlockDetectActivity.class);
        item3.addSubItem(subItem3_0);

        Level0Item item4 = new Level0Item("android其他", "");
        Fragmentation subItem4_0 = new Fragmentation("EventBus", EventRegisterActivity.class);
        Fragmentation subItem4_1 = new Fragmentation("增量更新", PatchUpdateActivity.class);
        Fragmentation subItem4_2 = new Fragmentation("微服务搭建", TinyServerActivity.class);
        Fragmentation subItem4_3 = new Fragmentation("学习资源tnt", AndroidAdvanceActivity.class);
        Fragmentation subItem4_4 = new Fragmentation("SyncAdapter同步（提高进程优先级）", SyncAdapterActivity.class);
        Fragmentation subItem4_5 = new Fragmentation("注解处理器", AnnotationProcessActivity.class);
        Fragmentation subItem4_6 = new Fragmentation("热修复", HotFixActivity.class);
        item4.addSubItem(subItem4_0);
        item4.addSubItem(subItem4_1);
        item4.addSubItem(subItem4_2);
        item4.addSubItem(subItem4_3);
        item4.addSubItem(subItem4_4);
        item4.addSubItem(subItem4_5);
        item4.addSubItem(subItem4_6);

        Level0Item item5 = new Level0Item("java并发编程", "");
        Fragmentation subItem5_0 = new Fragmentation("CountDownLatch", CountDownLatchActivity.class);
        Fragmentation subItem5_1 = new Fragmentation("ExecuteService", ExecuteServiceActivity.class);
        Fragmentation subItem5_2 = new Fragmentation("Future", FutureActivity.class);
        Fragmentation subItem5_3 = new Fragmentation("ReentrantLock", ReentrantLockActivity.class);
        Fragmentation subItem5_4 = new Fragmentation("ThreadLocal", ThreadLocalActivity.class);
        Fragmentation subItem5_5 = new Fragmentation("ThreadConcurrent", ThreadConcurrentActivity.class);
        item5.addSubItem(subItem5_0);
        item5.addSubItem(subItem5_1);
        item5.addSubItem(subItem5_2);
        item5.addSubItem(subItem5_3);
        item5.addSubItem(subItem5_4);
        item5.addSubItem(subItem5_5);

        Level0Item item6 = new Level0Item("音视频", "");
        Fragmentation subItem6_0 = new Fragmentation("通过两种方式绘制图片", MediaDrawImageActivity.class);
        Fragmentation subItem6_1 = new Fragmentation("使用AudioRecord采集音频PCM并保存到文件", MediaAudioRecordActivity.class);
        Fragmentation subItem6_2 = new Fragmentation("使用 AudioTrack 播放PCM音频", MediaAudioTrackActivity.class);
        Fragmentation subItem6_3 = new Fragmentation("使用 Camera API 采集视频数据", MediaCameraActivity.class);
        Fragmentation subItem6_4 = new Fragmentation("使用 MediaExtractor 和 MediaMuxer API 解析和封装 mp4 文件", MediaExecutorActivity.class);
        Fragmentation subItem6_5 = new Fragmentation("MediaRecorder和Camera实现视频录制（音频录制）及播放功能", MediaRecordActivity.class);
        Fragmentation subItem6_6 = new Fragmentation("Android 音乐(音效)播放方式总结", MediaVoiceActivity.class);
        Fragmentation subItem6_7 = new Fragmentation("使用VideoView播放视频", MediaVideoActivity.class);
        Fragmentation subItem6_8 = new Fragmentation("自定义播放器", MediaCusVideoActivity.class);
        Fragmentation subItem6_9 = new Fragmentation("ijkplayer播放器", IjkPlayerActivity.class);
        item6.addSubItem(subItem6_0);
        item6.addSubItem(subItem6_1);
        item6.addSubItem(subItem6_2);
        item6.addSubItem(subItem6_3);
        item6.addSubItem(subItem6_4);
        item6.addSubItem(subItem6_5);
        item6.addSubItem(subItem6_6);
        item6.addSubItem(subItem6_7);
        item6.addSubItem(subItem6_8);
        item6.addSubItem(subItem6_9);

        Level0Item item7 = new Level0Item("OpenGL ES", "");
        Fragmentation subItem7_0 = new Fragmentation("GLSurfaceView的探索", GLSurfaceActivity.class);
        Fragmentation subItem7_1 = new Fragmentation("openGL绘制形状", GL_DefineShapeActivity.class);
        item7.addSubItem(subItem7_0);
        item7.addSubItem(subItem7_1);

        Level0Item item8 = new Level0Item("Matrial Design", "");
        Fragmentation subItem8_0 = new Fragmentation("MD Button", MD_ButtonActivity.class);
        Fragmentation subItem8_1 = new Fragmentation("状态栏变化", MD_StatusBarActivity.class);
        Fragmentation subItem8_2 = new Fragmentation("recycleview动画", RecycleViewAnimActivity.class);
        Fragmentation subItem8_3 = new Fragmentation("recycleview装饰", RecycleViewDecorateActivity.class);
        Fragmentation subItem8_4 = new Fragmentation("viewOutline", ViewOutlineActivity.class);
        Fragmentation subItem8_5 = new Fragmentation("沉浸式", TranslucentBarActivity.class);
        Fragmentation subItem8_6 = new Fragmentation("SVG & VectorDrawable 详解", SvgActivity.class);
        Fragmentation subItem8_7 = new Fragmentation("MatraialShapeDrawable", MatraialShapeDrawableActivity.class);
        Fragmentation subItem8_8 = new Fragmentation("UI_FLAG", UiflagActivity.class);
        Fragmentation subItem8_9 = new Fragmentation("MotionLayout基本操作", MotionLayoutActivity.class);
        item8.addSubItem(subItem8_0);
        item8.addSubItem(subItem8_1);
        item8.addSubItem(subItem8_2);
        item8.addSubItem(subItem8_3);
        item8.addSubItem(subItem8_4);
        item8.addSubItem(subItem8_5);
        item8.addSubItem(subItem8_6);
        item8.addSubItem(subItem8_7);
        item8.addSubItem(subItem8_8);
        item8.addSubItem(subItem8_9);

        Level0Item item9 = new Level0Item("插件化", "");
        Fragmentation subItem9_0 = new Fragmentation("深入探讨java类加载器", ClassLoaderActivity.class);
        Fragmentation subItem9_1 = new Fragmentation("动态加载dex", DexLoaderActivity.class);
        Fragmentation subItem9_2 = new Fragmentation("动态加载技术加载已安装和未安装的apk", ApkLoaderActivity.class);
        Fragmentation subItem9_3 = new Fragmentation("动态加载Activity", ActivityLoaderActivity.class);
        item9.addSubItem(subItem9_0);
        item9.addSubItem(subItem9_1);
        item9.addSubItem(subItem9_2);
        item9.addSubItem(subItem9_3);

        Level0Item item10 = new Level0Item("JetPack", "");
        Fragmentation subItem10_0 = new Fragmentation("LiveData&ViewModel", LiveViewModelActivity.class);
        Fragmentation subItem10_1 = new Fragmentation("Navigation", JetpackNavigationActivity.class);
        Fragmentation subItem10_2 = new Fragmentation("LifeCycles", LifeCycleActivity.class);
        Fragmentation subItem10_3 = new Fragmentation("Room", RoomActivity.class);
        Fragmentation subItem10_4 = new Fragmentation("Kt", KtActivity.class);
        Fragmentation subItem10_5 = new Fragmentation("Paging", PagingActivity.class);
        Fragmentation subItem10_6 = new Fragmentation("ViewBinding", ViewBindingActivity.class);
        Fragmentation subItem10_7 = new Fragmentation("Worker", WorkerActivity.class);
        Fragmentation subItem10_8 = new Fragmentation("DataBinding", DataBindingActivity.class);
        Fragmentation subItem10_9 = new Fragmentation("PositionalDataSource", PositionalDataSourceActivity.class);
        Fragmentation subItem10_10 = new Fragmentation("PageKeyDataSource", PageKeyDataSourceActivity.class);
        Fragmentation subItem10_11 = new Fragmentation("ItemKeyDataSource", ItemKeyDataSourceActivity.class);
        Fragmentation subItem10_12 = new Fragmentation("BoundaryCallback", BoundaryActivity.class);
        item10.addSubItem(subItem10_0);
        item10.addSubItem(subItem10_1);
        item10.addSubItem(subItem10_2);
        item10.addSubItem(subItem10_3);
        item10.addSubItem(subItem10_4);
        item10.addSubItem(subItem10_5);
        item10.addSubItem(subItem10_6);
        item10.addSubItem(subItem10_7);
        item10.addSubItem(subItem10_8);
        item10.addSubItem(subItem10_9);
        item10.addSubItem(subItem10_10);
        item10.addSubItem(subItem10_11);
        item10.addSubItem(subItem10_12);

        Level0Item item11 = new Level0Item("Android动画", "");
        Fragmentation subItem11_0 = new Fragmentation("Activity转场动画", SceneTransitionAnimationActivity.class);
        Fragmentation subItem11_1 = new Fragmentation("基础转场动画", BasicTransitionAnimationActivity.class);
        Fragmentation subItem11_2 = new Fragmentation("自定义转场动画", CustomTransitionActivity.class);
        Fragmentation subItem11_3 = new Fragmentation("Drawable动画", DrawableActivity.class);
        Fragmentation subItem11_4 = new Fragmentation("Fragment转场动画", FragmentTransitionActivity.class);
        Fragmentation subItem11_5 = new Fragmentation("Interpolator", InterpolatorActivity.class);
        Fragmentation subItem11_6 = new Fragmentation("Motion", MotionActivity.class);
        Fragmentation subItem11_7 = new Fragmentation("展示效果动画", RevealEffectActivity.class);
        Fragmentation subItem11_8 = new Fragmentation("UnSplash动画", UnSplashActivity.class);

        item11.addSubItem(subItem11_0);
        item11.addSubItem(subItem11_1);
        item11.addSubItem(subItem11_2);
        item11.addSubItem(subItem11_3);
        item11.addSubItem(subItem11_4);
        item11.addSubItem(subItem11_5);
        item11.addSubItem(subItem11_6);
        item11.addSubItem(subItem11_7);
        item11.addSubItem(subItem11_8);

        list.add(item0);
        list.add(item1);
        list.add(item2);
        list.add(item3);
        list.add(item4);
        list.add(item5);
        list.add(item6);
        list.add(item7);
        list.add(item8);
        list.add(item9);
        list.add(item10);
        list.add(item11);

        return list;
    }

    public static List<BaseNode> getSourceItems() {
        List list = new ArrayList();

        Level0Item item0 = new Level0Item("客户端项目", "");

        Level0Item item1 = new Level0Item("优质博客(长按打开资源~ ~!)", "");
        Fragmentation subItem1_0 = new Fragmentation("终极组件化框架项目方案详解-https://blog.csdn.net/c10WTiybQ1Ye3/article/details/78979879", null);
        item1.addSubItem(subItem1_0);

        list.add(item0);
        list.add(item1);

        return list;
    }
}
