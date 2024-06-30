package com.leandrolcd.onvifcamera.soap

import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import kotlinx.serialization.Serializable

@Serializable
@XmlSerialName("Service", "http://www.onvif.org/ver10/device/wsdl", "tds")
internal class OnvifService(
    @XmlElement(true)
    @XmlSerialName("Namespace", "http://www.onvif.org/ver10/device/wsdl", "tds")
    val namespace: String,
    @XmlElement(true)
    @XmlSerialName("XAddr", "http://www.onvif.org/ver10/device/wsdl", "tds")
    val address: String,
    val version: Version,
)