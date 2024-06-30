package com.leandrolcd.data.repositories

import com.ivanempire.lighthouse.LighthouseClient
import com.leandrolcd.core.di.DispatchersIO
import com.leandrolcd.data.dto.OnvifCachedDevice
import com.leandrolcd.data.dto.CameraInformation
import com.leandrolcd.domain.models.DeviceInformation
import com.leandrolcd.domain.useCase.IDiscoveryManagerRepository
import com.leandrolcd.onvifcamera.OnvifDevice
import com.leandrolcd.onvifcamera.network.OnvifDiscoveryManager
import io.ktor.http.Url
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DiscoveryManagerRepository @Inject constructor(
    private val lighthouseClient: LighthouseClient,
    private val onvifDiscoveryManager: OnvifDiscoveryManager,
    @DispatchersIO private val dispatcher: CoroutineDispatcher
) : IDiscoveryManagerRepository {

    override fun discoveryDevices(): Flow<List<DeviceInformation>> = flow {
        val cachedOnvifDevices = mutableMapOf<String, OnvifCachedDevice>()
        val friendlyNameMap = mutableMapOf<String, String>()
        withContext(dispatcher){
            combine(
                onvifDiscoveryManager.discoverDevices(2),
                lighthouseClient.discoverDevices(),
            ) { onvifDevices, ssdpDevices ->
                onvifDevices.mapNotNull { onvifDevice ->
                    var friendlyName = onvifDevice.id
                    val cachedOnvifDevice = cachedOnvifDevices[onvifDevice.id]
                    val endpoint = cachedOnvifDevice?.endpoint
                        ?: onvifDevice.addresses
                            .firstOrNull { OnvifDevice.isReachableEndpoint(it) }
                            ?.also { endpoint ->
                                cachedOnvifDevices[onvifDevice.id] =
                                    OnvifCachedDevice(
                                        onvifDevice.addresses.map { Url(it).host },
                                        endpoint
                                    )
                            }
                        ?: return@mapNotNull null
                    val ssdpFriendlyName = friendlyNameMap.getOrElse(onvifDevice.id) {
                        ssdpDevices.firstNotNullOfOrNull { ssdpDevice ->
                            if (onvifDevice.addresses.any { Url(it).host == ssdpDevice.location.host }) {
                                val detailedDevice = lighthouseClient.retrieveDescription(ssdpDevice)
                                friendlyNameMap[onvifDevice.id] = detailedDevice.friendlyName
                                detailedDevice.friendlyName
                            } else {
                                null
                            }
                        }
                    }
                    if (ssdpFriendlyName != null) {
                        friendlyName = ssdpFriendlyName
                    }
                    DeviceInformation(friendlyName, onvifDevice.id, endpoint)
                }
            }
                .collect {
                    emit(it)
                }
        }
    }

}