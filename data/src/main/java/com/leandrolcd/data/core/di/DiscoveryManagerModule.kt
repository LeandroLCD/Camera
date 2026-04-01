package com.leandrolcd.data.core.di

import android.content.Context
import android.util.Log
import com.ivanempire.lighthouse.LighthouseClient
import com.ivanempire.lighthouse.LighthouseLogger
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
        return LighthouseClient
            .Builder(context)
            .setLogger(object : LighthouseLogger() {
                override fun logErrorMessage(tag: String, message: String, ex: Throwable?) {

                    Log.e(tag, message)
                    ex?.printStackTrace()
                    super.logErrorMessage(tag, message, ex)
                }

                override fun logStatusMessage(tag: String, message: String) {
                    Log.d(tag, message)
                    super.logStatusMessage(tag, message)
                }
            })
            .setRetryCount(3)
            .build()
    }

    @Provides
    @Singleton
    fun provideOnvifDiscoveryManager(@ApplicationContext context:Context): OnvifDiscoveryManager {
        return discoveryManager(context)
    }
}