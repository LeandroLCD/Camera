package com.leandrolcd.camera.domain.useCase

import javax.inject.Inject

class SaveDeviceUseCase  @Inject constructor(private val repository:IOnvifDeviceRepository) {
    operator fun invoke(serial:String, canal:Int = 0) = repository.saveDevice(serial, canal)
}