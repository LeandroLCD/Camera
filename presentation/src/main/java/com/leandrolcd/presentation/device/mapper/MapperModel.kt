package com.leandrolcd.presentation.device.mapper

import com.leandrolcd.domain.models.DeviceInformation
import com.leandrolcd.presentation.device.model.DeviceInfo
import javax.inject.Inject

class MapperDomain @Inject constructor() {
    fun toModel(device: DeviceInformation): DeviceInfo{
        return device.run {
            DeviceInfo(
                username = username,
                serial = serial,
                host = host
            )
        }
    }
}