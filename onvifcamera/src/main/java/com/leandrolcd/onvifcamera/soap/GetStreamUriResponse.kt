package com.leandrolcd.onvifcamera.soap

import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import kotlinx.serialization.Serializable

@Serializable
@XmlSerialName("GetStreamUriResponse", "http://www.onvif.org/ver20/media/wsdl", "tr2")
internal class GetStreamUriResponse(
    @XmlElement(true)
    @XmlSerialName("Uri", "http://www.onvif.org/ver20/media/wsdl", "tr2")
    val uri: String
)
@Serializable
@XmlSerialName("GetStreamUriResponse", "http://www.onvif.org/ver10/media/wsdl", "trt")
internal data class GetStreamMediaUriResponse(
    @XmlElement(true)
    @XmlSerialName("MediaUri", "http://www.onvif.org/ver10/media/wsdl", "trt")
    val mediaUri: MediaUri
)
@Serializable
@XmlSerialName("MediaUri", "http://www.onvif.org/ver10/media/wsdl", "trt")
internal data class MediaUri(
    @XmlElement(true)
    @XmlSerialName("Uri", "http://www.onvif.org/ver10/schema", "tt")
    val uri: String,

    @XmlElement(true)
    @XmlSerialName("InvalidAfterConnect", "http://www.onvif.org/ver10/schema", "tt")
    val invalidAfterConnect: Boolean,

    @XmlElement(true)
    @XmlSerialName("InvalidAfterReboot", "http://www.onvif.org/ver10/schema", "tt")
    val invalidAfterReboot: Boolean,

    @XmlElement(true)
    @XmlSerialName("Timeout", "http://www.onvif.org/ver10/schema", "tt")
    val timeout: String
)