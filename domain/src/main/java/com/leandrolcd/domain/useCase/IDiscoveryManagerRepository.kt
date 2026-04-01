package com.leandrolcd.domain.useCase

import com.leandrolcd.domain.models.DeviceInformation
import kotlinx.coroutines.flow.Flow


interface IDiscoveryManagerRepository{

    fun discoveryDevices(): Flow<List<DeviceInformation>>

}

