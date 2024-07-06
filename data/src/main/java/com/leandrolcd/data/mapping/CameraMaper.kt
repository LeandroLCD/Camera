package com.leandrolcd.data.mapping

import com.leandrolcd.domain.models.Device


fun com.leandrolcd.core.local.Entities.DeviceEntity.toModel(): Device {
    return Device(id, canal, snapshot)
}