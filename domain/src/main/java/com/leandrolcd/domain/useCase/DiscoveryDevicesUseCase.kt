package com.leandrolcd.domain.useCase

import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DiscoveryDevicesUseCase @Inject constructor(private val repository: IDiscoveryManagerRepository) {
    operator  fun invoke() = repository.discoveryDevices()
}