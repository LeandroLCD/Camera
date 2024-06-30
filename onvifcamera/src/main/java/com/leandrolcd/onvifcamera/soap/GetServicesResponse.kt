package com.leandrolcd.onvifcamera.soap

import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import kotlinx.serialization.Serializable

@Serializable
@XmlSerialName("GetServicesResponse", "http://www.onvif.org/ver10/device/wsdl", "tds")
internal class GetServicesResponse(
    @XmlElement(true)
    val services: List<OnvifService>
)