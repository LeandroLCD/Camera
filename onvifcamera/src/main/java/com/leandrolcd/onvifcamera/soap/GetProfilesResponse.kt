package com.leandrolcd.onvifcamera.soap

import nl.adaptivity.xmlutil.serialization.XmlSerialName
import kotlinx.serialization.Serializable

@Serializable
@XmlSerialName("GetProfilesResponse", "http://www.onvif.org/ver10/media/wsdl", "trt")
internal class GetProfilesResponse(
    val profiles: List<Profiles>
)