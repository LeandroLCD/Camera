package com.leandrolcd.onvifcamera.soap

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root


@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s")
data class ProbeEnvelope(

    @field:Element(name = "Body", required = true)
    var body: SoapBody? = null
)

@Root(name = "Body", strict = false)
data class SoapBody(
    @field:Element(name = "ProbeMatches", required = false)
    @field:Namespace(reference = "http://schemas.xmlsoap.org/ws/2005/04/discovery", prefix = "d")
    var probeMatches: ProbeMatches? = null
)

@Root(name = "ProbeMatches", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/ws/2005/04/discovery", prefix = "d")
data class ProbeMatches(
    @field:ElementList(entry = "ProbeMatch", inline = true, required = false)
    var matches: List<ProbeMatch>? = null
)

@Root(name = "ProbeMatch", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/ws/2005/04/discovery", prefix = "d")
data class ProbeMatch(

    @field:Element(name = "EndpointReference", required = false)
    @field:Namespace(reference = "http://schemas.xmlsoap.org/ws/2004/08/addressing", prefix = "wsa")
    var endpointReference: EndpointReference? = null,

    @field:Element(name = "Types", required = false)
    var types: String? = null,

    @field:Element(name = "Scopes", required = false)
    var scopes: String? = null,

    @field:Element(name = "XAddrs", required = false)
    var xAddrs: String? = null,

    @field:Element(name = "MetadataVersion", required = false)
    var metadataVersion: Int? = null
)


@Root(name = "EndpointReference", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/ws/2004/08/addressing", prefix = "wsa")
data class EndpointReference(
    @field:Element(name = "Address", required = false)
    var address: String? = null
)
