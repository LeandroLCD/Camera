package com.leandrolcd.onvifcamera.network

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import com.leandrolcd.onvifcamera.DiscoveredOnvifDevice
import com.leandrolcd.onvifcamera.parseOnvifProbeResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.InetAddress

class OnvifDiscoveryManagerImpl(context: Context) : OnvifDiscoveryManager {
    private val wifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val socketListener = AndroidSocketListener(wifiManager)

    override fun discoverDevices(
        retryCount: Int,
        scope: CoroutineScope
    ): Flow<List<DiscoveredOnvifDevice>> {
        require(retryCount > 0) { "Retry count must be greater than 0" }

        val discoveredDevices =
            MutableStateFlow<Map<InetAddress, DiscoveredOnvifDevice>>(emptyMap())

        scope.launch {
            socketListener.listenForPackets(retryCount)
                .collect { packet ->
                    val data = packet.data.copyOf(packet.length).decodeToString()
                    Log.d("OnvifDiscoveryManager", "Discovered device: $data")

                    runCatching {
                        val result = parseOnvifProbeResponse(data)
                        Log.d("OnvifDiscoveryManager", "parseOnvifProbeResponse: $result")

                        if (result != null) {
                            val probeMatch = result
                            val device = DiscoveredOnvifDevice(
                                id = probeMatch.endpointReference?.address.orEmpty(),
                                types = probeMatch.types?.split(" ") ?: emptyList(),
                                scopes = probeMatch.scopes?.split(" ") ?: emptyList(),
                                addresses = probeMatch.xAddrs?.split(" ") ?: emptyList(),
                            )

                            discoveredDevices.update { it + (packet.address to device) }
                        }
                    }.onFailure { e ->
                        Log.e("OnvifDiscoveryManager", "Failed to parse probe response", e)
                    }
                }
        }

        return discoveredDevices.map { it.values.toList() }
    }
}


