package com.leandrolcd.onvifcamera.network

import android.net.wifi.WifiManager
import android.util.Log
import com.leandrolcd.onvifcamera.OnvifCommands
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.MulticastSocket
import java.util.UUID

internal class AndroidSocketListener(
    private val wifiManager: WifiManager,
) : SocketListener {

    private val multicastLock: WifiManager.MulticastLock by lazy {
        wifiManager.createMulticastLock("OnvifCamera")
    }

    private val multicastAddress: InetAddress by lazy {
        InetAddress.getByName(MULTICAST_ADDRESS)
    }

    override fun setupSocket(): MulticastSocket {
        multicastLock.setReferenceCounted(true)
        multicastLock.acquire()

        val multicastSocket = MulticastSocket(null).apply {
            reuseAddress = true
            broadcast = true
            loopbackMode = true
            bind(InetSocketAddress(MULTICAST_PORT))
        }

        try {
            multicastSocket.joinGroup(multicastAddress)
            Log.d(TAG, "MulticastSocket has been setup")
        } catch (ex: Exception) {
            Log.e(TAG, "Could not join multicast group", ex)
            multicastSocket.close()
            if (multicastLock.isHeld) multicastLock.release()
            throw ex
        }

        return multicastSocket
    }

    override fun listenForPackets(retryCount: Int): Flow<DatagramPacket> = flow {
        val multicastSocket = setupSocket()

        try {
            val messageId = UUID.randomUUID()
            val requestMessage = OnvifCommands.probeCommand(messageId.toString()).toByteArray()
            val requestDatagram =
                DatagramPacket(requestMessage, requestMessage.size, multicastAddress, MULTICAST_PORT)

            repeat(retryCount) {
                multicastSocket.send(requestDatagram)
                kotlinx.coroutines.delay(100)
            }

            val buffer = ByteArray(MULTICAST_DATAGRAM_SIZE)
            while (currentCoroutineContext().isActive) {
                val packet = DatagramPacket(buffer, buffer.size)
                withContext(kotlinx.coroutines.Dispatchers.IO) {
                    if (multicastSocket.isClosed) {
                        return@withContext
                    }
                    multicastSocket.receive(packet)
                }
                emit(packet)
            }
        } finally {
            teardownSocket(multicastSocket)
        }
    }
    override fun teardownSocket(multicastSocket: MulticastSocket) {
        try {
            if (!multicastSocket.isClosed) {
                multicastSocket.leaveGroup(multicastAddress)
                multicastSocket.close()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing socket resources", e)
        } finally {
            if (multicastLock.isHeld) multicastLock.release()
        }
    }

    private companion object {
        const val MULTICAST_DATAGRAM_SIZE = 2048
        const val MULTICAST_PORT = 3702
        const val MULTICAST_ADDRESS = "239.255.255.250"
        const val TAG = "AndroidSocketListener"
    }
}
