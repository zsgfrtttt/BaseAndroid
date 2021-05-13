package com.hydbest.baseandroid.activity.jetpack.hilt

import com.csz.video.media.SlotValue
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object provideModule {

    //创建跨模块对象
    @Provides
    fun create(): SlotValue{
        return SlotValue("http://baidu.com","http://sina.com",false)
    }
}