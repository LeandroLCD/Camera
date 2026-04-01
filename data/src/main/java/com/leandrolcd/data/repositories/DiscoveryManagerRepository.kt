package com.leandrolcd.data.repositories

import com.leandrolcd.data.core.di.DispatchersIO
import com.leandrolcd.data.dto.OnvifCachedDevice
import com.leandrolcd.domain.models.DeviceInformation
import com.leandrolcd.domain.useCase.IDiscoveryManagerRepository
import com.leandrolcd.onvifcamera.DiscoveredOnvifDevice
import com.leandrolcd.onvifcamera.OnvifDevice
import com.leandrolcd.onvifcamera.network.OnvifDiscoveryManager
import io.ktor.http.Url
import io.ktor.http.hostWithPort
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class DiscoveryManagerRepository @Inject constructor(
    private val onvifDiscoveryManager: OnvifDiscoveryManager,
    @DispatchersIO private val dispatcher: CoroutineDispatcher
) : IDiscoveryManagerRepository {

    private val cachedOnvifDevices = ConcurrentHashMap<String, OnvifCachedDevice>()

    override fun discoveryDevices(): Flow<List<DeviceInformation>> =
        onvifDiscoveryManager
            .discoverDevices( 2)
            .map { devices ->
                devices.mapNotNull { device -> mapToDeviceInformation(device) }
            }
            .flowOn(dispatcher)


    private suspend fun mapToDeviceInformation(device: DiscoveredOnvifDevice): DeviceInformation? {
        val cached = cachedOnvifDevices[device.id]

        val endpoint = cached?.endpoint
            ?: resolveReachableEndpoint(device)?.also { resolved ->
                cachedOnvifDevices[device.id] = OnvifCachedDevice(
                    hosts = device.addresses.map { Url(it).host },
                    endpoint = resolved
                )
            }
            ?: return null

        val url = Url(endpoint)
        return DeviceInformation(
            username = "admin",
            serial = device.id.removePrefix("urn:uuid:").substringBeforeLast('-'),
            host = url.hostWithPort
        )
    }

    private suspend fun resolveReachableEndpoint(device: DiscoveredOnvifDevice): String? {
        return device.addresses.firstOrNull { OnvifDevice.isReachableEndpoint(it) }
    }
}
