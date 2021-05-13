package com.hydbest.baseandroid.activity.jetpack.hilt

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
interface InjectModule {

    //生成接口实例
    @Binds
    fun creatDog(dog:Dog):Animal

    @AnnoDog
    @Binds
    fun creatDogByAnno(dog:Dog):Animal

    @AnnoCat
    @Binds
    fun creatCatByAnno(cat: Cat):Animal

}