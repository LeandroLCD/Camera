package com.leandrolcd.domain.useCase

import com.leandrolcd.domain.models.Device
import javax.inject.Inject

class UpdateDeviceUseCase @Inject constructor(private val repository: IDeviceRepository) {
    operator  fun invoke(device: Device) = repository.updateDevice(device)
}