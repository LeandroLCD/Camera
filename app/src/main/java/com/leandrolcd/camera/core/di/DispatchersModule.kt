package com.leandrolcd.camera.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @DispatchersIO
    @Provides
    fun dispatcherIoProvider(): CoroutineDispatcher = Dispatchers.IO

    @DispatchersDefault
    @Provides
    fun dispatcherDefaultProvider(): CoroutineDispatcher = Dispatchers.Default
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DispatchersIO

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DispatchersDefault