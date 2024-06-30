package com.leandrolcd.data.di

import android.content.Context
import com.ivanempire.lighthouse.LighthouseClient
import com.leandrolcd.onvifcamera.network.OnvifDiscoveryManager
import com.leandrolcd.onvifcamera.network.discoveryManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DiscoveryManagerModule {
    @Provides
    @Singleton
    fun provideLighthouseClient(@ApplicationContext context:Context): LighthouseClient{
        return LighthouseClient(context, 3)
    }

    @Provides
    @Singleton
    fun provideOnvifDiscoveryManager(@ApplicationContext context:Context): OnvifDiscoveryManager {
        return discoveryManager(context)
    }
}