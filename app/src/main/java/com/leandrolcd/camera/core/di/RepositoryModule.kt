package com.leandrolcd.camera.core.di

import com.leandrolcd.camera.data.DeviceRepository
import com.leandrolcd.camera.data.OnvifDeviceRepository
import com.leandrolcd.camera.domain.useCase.IDeviceRepository
import com.leandrolcd.camera.domain.useCase.IOnvifDeviceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindsOnvifDevice(repository: OnvifDeviceRepository):IOnvifDeviceRepository


    @Singleton
    @Binds
    abstract fun bindsDevice(repository: DeviceRepository): IDeviceRepository
}