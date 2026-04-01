package com.leandrolcd.onvifcamera

import android.util.Log
import com.leandrolcd.onvifcamera.soap.ProbeEnvelope
import com.leandrolcd.onvifcamera.soap.ProbeMatch
import com.leandrolcd.onvifcamera.soap.ProfileEnvelope
import com.leandrolcd.onvifcamera.soap.ServiceEnvelope
import com.leandrolcd.onvifcamera.soap.ServiceInformationEnvelope
import com.leandrolcd.onvifcamera.soap.SnapshotEnvelope
import com.leandrolcd.onvifcamera.soap.StreamMediaEnvelope
import com.leandrolcd.onvifcamera.soap.StreamUriEnvelope
import org.simpleframework.xml.core.Persister
import java.io.StringReader


private inline fun <reified T : Any> parseSoap(xmlString: String): T {
    val serializer = Persister()
    return serializer.read(T::class.java, StringReader(xmlString))
}

internal fun parseOnvifProfiles(input: String): List<MediaProfile> {
    val result = parseSoap<ProfileEnvelope>(input).body?.content?.profiles ?: emptyList()

    Log.d("CameraViewModel", "parseOnvifProfiles: $result")
    return result.map {
        MediaProfile(it.name.orEmpty(), it.token.orEmpty(), it.videoEncoderConfig?.encoding.orEmpty())
    }
}

internal fun parseOnvifStreamUri(input: String): String {
    return try {
        parseSoap<StreamUriEnvelope>(input).body.content.uri
    } catch (_: Exception) {
        parseSoap<StreamMediaEnvelope>(xmlString = input).body.content.mediaUri.uri
    }

}

internal fun parseOnvifSnapshotUri(input: String): String {
    val result = parseSoap<SnapshotEnvelope>(input).body.content.mediaUri
    return result.uri
}

internal fun parseOnvifServices(input: String): Map<String, String> {
    return parseSoap<ServiceEnvelope>(input).body?.service?.services?.associate {
        it.namespace.toString() to it.xAddr.orEmpty()
    } ?: emptyMap()
}

internal fun parseOnvifProbeResponse(input: String): ProbeMatch? {
    println(input)
    return parseSoap<ProbeEnvelope>(input).body?.probeMatches?.matches?.firstOrNull()
}

internal fun parseOnvifDeviceInformation(input: String): OnvifDeviceInformation {
    val result = parseSoap<ServiceInformationEnvelope>(input).body?.service
    return OnvifDeviceInformation(
        manufacturer = result?.manufacturer.orEmpty(),
        model = result?.model.orEmpty(),
        firmwareVersion = result?.firmwareVersion.orEmpty(),
        serialNumber = result?.serialNumber.orEmpty(),
        hardwareId = result?.hardwareId.orEmpty(),
    )
}
