package com.leandrolcd.data.mapping

import com.leandrolcd.domain.models.Device


fun com.leandrolcd.core.local.Entities.DeviceEntity.toModel(isOnline: Boolean): Device {
    return Device(
        id = id,
        serial = serial,
        alias = alias,
        canal = canal,
        isConnected = isOnline,
        snapshot = snapshot
    )
}