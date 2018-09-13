package com.hydbest.baseandroid.util;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.hydbest.baseandroid.activity.Media.MediaAudioRecordActivity;
import com.hydbest.baseandroid.activity.Media.MediaAudioTrackActivity;
import com.hydbest.baseandroid.activity.Media.MediaDrawImageActivity;
import com.hydbest.baseandroid.activity.concurrent.CountDownLatchActivity;
import com.hydbest.baseandroid.activity.concurrent.ExecuteServiceActivity;
import com.hydbest.baseandroid.activity.concurrent.FutureActivity;
import com.hydbest.baseandroid.activity.concurrent.ReentrantLockActivity;
import com.hydbest.baseandroid.activity.concurrent.ThreadConcurrentActivity;
import com.hydbest.baseandroid.activity.concurrent.ThreadLocalActivity;
import com.hydbest.baseandroid.activity.cus_view.AdActivity;
import com.hydbest.baseandroid.activity.cus_view.LargeImageActivity;
import com.hydbest.baseandroid.activity.cus_view.MultiTouchActivity;
import com.hydbest.baseandroid.activity.cus_viewgroup.ConstraintActivity;
import com.hydbest.baseandroid.activity.cus_viewgroup.ImageWatcherActivity;
import com.hydbest.baseandroid.activity.cus_viewgroup.LeftDrawerLayoutActivity;
import com.hydbest.baseandroid.activity.cus_viewgroup.NavigationActivity;
import com.hydbest.baseandroid.activity.cus_viewgroup.NestScrollActivity;
import com.hydbest.baseandroid.activity.cus_viewgroup.ViewpageActivity;
import com.hydbest.baseandroid.activity.event.EventRegisterActivity;
import com.hydbest.baseandroid.activity.foundation.BlockDetectActivity;
import com.hydbest.baseandroid.activity.foundation.FileProviderActivity;
import com.hydbest.baseandroid.activity.foundation.LogActivity;
import com.hydbest.baseandroid.activity.foundation.RuntimePermissonActivity;
import com.hydbest.baseandroid.entity.Fragmentation;
import com.hydbest.baseandroid.entity.Level0Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csz on 2018/9/13.
 * 数据生成
 */

public class DataGeneration {

    public static List<MultiItemEntity> getItems() {
        List list = new ArrayList();

        Level0Item item0 = new Level0Item("android基础API", "");
        Fragmentation subItem0_0 = new Fragmentation("运行时权限", RuntimePermissonActivity.class);
        Fragmentation subItem0_1 = new Fragmentation("Log库原理", LogActivity.class);
        Fragmentation subItem0_2 = new Fragmentation("FileProvider", FileProviderActivity.class);
        item0.addSubItem(subItem0_0);
        item0.addSubItem(subItem0_1);
        item0.addSubItem(subItem0_2);

        Level0Item item1 = new Level0Item("android自定义View", "");
        Fragmentation subItem1_0 = new Fragmentation("仿知乎广告", AdActivity.class);
        Fragmentation subItem1_1 = new Fragmentation("加载查看超大图片", LargeImageActivity.class);
        Fragmentation subItem1_2 = new Fragmentation("多触点的View", MultiTouchActivity.class);
        item1.addSubItem(subItem1_0);
        item1.addSubItem(subItem1_1);
        item1.addSubItem(subItem1_2);

        Level0Item item2 = new Level0Item("android自定义ViewGroup", "");
        Fragmentation subItem2_0 = new Fragmentation("constrainLayout", ConstraintActivity.class);
        Fragmentation subItem2_1 = new Fragmentation("仿微信朋友圈图片查看", ImageWatcherActivity.class);
        Fragmentation subItem2_2 = new Fragmentation("LeftDrawerLayout", LeftDrawerLayoutActivity.class);
        Fragmentation subItem2_3 = new Fragmentation("NavigationView", NavigationActivity.class);
        Fragmentation subItem2_4 = new Fragmentation("嵌套滚动", NestScrollActivity.class);
        Fragmentation subItem2_5 = new Fragmentation("Viewpager transformer", ViewpageActivity.class);
        item2.addSubItem(subItem2_0);
        item2.addSubItem(subItem2_1);
        item2.addSubItem(subItem2_2);
        item2.addSubItem(subItem2_3);
        item2.addSubItem(subItem2_4);
        item2.addSubItem(subItem2_5);

        Level0Item item3 = new Level0Item("android性能优化", "");
        Fragmentation subItem3_0 = new Fragmentation("主线程阻塞检测", BlockDetectActivity.class);
        item3.addSubItem(subItem3_0);

        Level0Item item4 = new Level0Item("android其他", "");
        Fragmentation subItem4_0 = new Fragmentation("EventBus", EventRegisterActivity.class);
        item4.addSubItem(subItem4_0);

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

        Level0Item item6 = new Level0Item("音视频初级", "");
        Fragmentation subItem6_0 = new Fragmentation("通过两种方式绘制图片", MediaDrawImageActivity.class);
        Fragmentation subItem6_1 = new Fragmentation("使用AudioRecord采集音频PCM并保存到文件", MediaAudioRecordActivity.class);
        Fragmentation subItem6_2 = new Fragmentation("使用 AudioTrack 播放PCM音频", MediaAudioTrackActivity.class);

        item6.addSubItem(subItem6_0);
        item6.addSubItem(subItem6_1);
        item6.addSubItem(subItem6_2);

        list.add(item0);
        list.add(item1);
        list.add(item2);
        list.add(item3);
        list.add(item4);
        list.add(item5);
        list.add(item6);

        return list;
    }
}
