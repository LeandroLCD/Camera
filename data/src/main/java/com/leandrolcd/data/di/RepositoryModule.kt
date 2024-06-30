package com.leandrolcd.data.di

import com.leandrolcd.data.repositories.DeviceRepository
import com.leandrolcd.data.repositories.DiscoveryManagerRepository
import com.leandrolcd.data.repositories.OnvifDeviceRepository
import com.leandrolcd.domain.useCase.IDeviceRepository
import com.leandrolcd.domain.useCase.IDiscoveryManagerRepository
import com.leandrolcd.domain.useCase.IOnvifDeviceRepository
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
    abstract fun bindsOnvifDevice(repository: OnvifDeviceRepository): IOnvifDeviceRepository


    @Singleton
    @Binds
    abstract fun bindsDevice(repository: DeviceRepository): IDeviceRepository

    @Singleton
    @Binds
    abstract fun bindsDiscoveryDevices(repository: DiscoveryManagerRepository): IDiscoveryManagerRepository
}