package com.leandrolcd.domain.useCase

import com.leandrolcd.domain.models.DeviceInformation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DiscoveryDevicesUseCase @Inject constructor(private val repository: IDiscoveryManagerRepository) {


    operator  fun invoke(): Flow<List<DeviceInformation>> {
        return repository.discoveryDevices()
    }

}