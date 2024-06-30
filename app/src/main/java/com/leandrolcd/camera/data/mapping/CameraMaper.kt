package com.leandrolcd.camera.data.mapping

import com.leandrolcd.camera.core.local.Entities.DeviceEntity
import com.leandrolcd.camera.domain.models.Device


fun DeviceEntity.toModel():Device{
    return Device(id, canal, snapshot)
}