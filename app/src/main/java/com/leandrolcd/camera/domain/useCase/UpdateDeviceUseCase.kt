package com.leandrolcd.camera.domain.useCase

import com.leandrolcd.camera.domain.models.Device
import javax.inject.Inject

class UpdateDeviceUseCase @Inject constructor(private val repository: IDeviceRepository) {
    operator  fun invoke(device: Device) = repository.updateDevice(device)
}