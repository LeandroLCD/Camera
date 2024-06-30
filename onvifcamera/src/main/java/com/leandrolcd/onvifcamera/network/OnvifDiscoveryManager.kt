package com.leandrolcd.onvifcamera.network

import com.leandrolcd.onvifcamera.DiscoveredOnvifDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

interface OnvifDiscoveryManager {
    fun discoverDevices(
        retryCount: Int = 1,
        scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    ): Flow<List<DiscoveredOnvifDevice>>
}