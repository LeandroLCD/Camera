@file:Suppress("DEPRECATION")

package com.leandrolcd.onvifcamera


import com.leandrolcd.onvifcamera.soap.Envelope
import com.leandrolcd.onvifcamera.soap.GetServicesResponse
import com.leandrolcd.onvifcamera.soap.ProbeMatch
import com.leandrolcd.onvifcamera.soap.ProbeMatches
import com.leandrolcd.onvifcamera.soap.GetDeviceInformationResponse
import com.leandrolcd.onvifcamera.soap.GetProfilesResponse
import com.leandrolcd.onvifcamera.soap.GetSnapshotUriResponse
import com.leandrolcd.onvifcamera.soap.GetStreamMediaUriResponse
import com.leandrolcd.onvifcamera.soap.GetStreamUriResponse
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.serialization.UnknownChildHandler
import nl.adaptivity.xmlutil.serialization.XML

@OptIn(ExperimentalXmlUtilApi::class)
private inline fun <reified T : Any> parseSoap(input: String): T {
    val module = SerializersModule {
        polymorphic(Any::class) {
            subclass(T::class, serializer())
        }
    }

    val xml = XML(module) {
        xmlDeclMode = XmlDeclMode.Minimal
        autoPolymorphic = true
        unknownChildHandler = UnknownChildHandler { _, _, _, _, _ -> emptyList() }
    }
    val serializer = serializer<Envelope<T>>()

    return xml.decodeFromString(serializer, input).data
}

internal fun parseOnvifProfiles(input: String): List<MediaProfile> {
    val result = parseSoap<GetProfilesResponse>(input)

    return result.profiles.map {
        MediaProfile(it.name, it.token, it.encoder.encoding)
    }
}

internal fun parseOnvifStreamUri(input: String): String {
  return  try {
        parseSoap<GetStreamUriResponse>(input).uri
    }catch (e: Exception){
        parseSoap<GetStreamMediaUriResponse>(input = input).mediaUri.uri
    }

}

internal fun parseOnvifSnapshotUri(input: String): String {
    val result = parseSoap<GetSnapshotUriResponse>(input)
    return result.uri
}

internal fun parseOnvifServices(input: String): Map<String, String> {
    return parseSoap<GetServicesResponse>(input).services.associate {
        it.namespace to it.address
    }
}

internal fun parseOnvifProbeResponse(input: String): List<ProbeMatch> {
    return parseSoap<ProbeMatches>(input).matches
}

internal fun parseOnvifDeviceInformation(input: String): OnvifDeviceInformation {
    val result = parseSoap<GetDeviceInformationResponse>(input)
    return OnvifDeviceInformation(
        manufacturer = result.manufacturer,
        model = result.model,
        firmwareVersion = result.firmwareVersion,
        serialNumber = result.serialNumber,
        hardwareId = result.hardwareId,
    )
}
