package com.leandrolcd.camera.data.dto

import com.leandrolcd.onvifcamera.OnvifDevice

class DeviceDto (
    val user:String,
    val password:String,
    val url:String,
    val onvif: OnvifDevice
)