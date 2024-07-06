package com.leandrolcd.onvifcamera.soap

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

import org.simpleframework.xml.Element

// ----------------------
// Clases específicas de GetServicesResponse
// ----------------------
@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s")
data class ServiceEnvelope(

    @field:Element(name = "Body", required = true)
    var body: ServiceBody? = null
)

@Root(name = "Body", strict = false)
data class ServiceBody(
    @field:Element(name = "GetServicesResponse", required = false)
    @field:Namespace(reference = "http://schemas.xmlsoap.org/ws/2005/04/discovery", prefix = "d")
    var service: GetServicesResponse? = null
)
@Root(name = "GetServicesResponse", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/device/wsdl", prefix = "tds")
data class GetServicesResponse(

    @field:ElementList(entry = "Service", inline = true, required = false)
    var services: List<Service>? = null
)

@Root(name = "Service", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/device/wsdl", prefix = "tds")
data class Service(

    @field:Element(name = "Namespace", required = false)
    var namespace: String? = null,

    @field:Element(name = "XAddr", required = false)
    var xAddr: String? = null,

    @field:Element(name = "Version", required = false)
    var version: Version? = null
)

@Root(name = "Version", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
data class Version(

    @field:Element(name = "Major", required = false)
    var major: Int? = null,

    @field:Element(name = "Minor", required = false)
    var minor: Int? = null
)

