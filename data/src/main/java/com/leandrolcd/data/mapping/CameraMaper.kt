package com.leandrolcd.data.mapping

import com.leandrolcd.data.core.local.Entities.DeviceEntity
import com.leandrolcd.domain.models.Device


fun DeviceEntity.toModel(isOnline: Boolean): Device {
    return Device(
        id = id,
        serial = serial,
        alias = alias,
        canal = canal,
        isConnected = isOnline,
        snapshot = snapshot
    )
}