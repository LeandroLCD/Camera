package com.leandrolcd.onvifcamera.soap

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root


@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s")
data class StreamUriEnvelope(
    @field:Element(name = "Body", required = true)
    var body: StreamUriBody
)

@Root(name = "Body", strict = false)
data class StreamUriBody(
    @field:Element(name= "GetStreamUriResponse", required = false)
    var content: GetStreamUriResponse
)
@Root(name = "GetStreamUriResponse", strict = false)
@Namespace(reference = "http://www.onvif.org/ver20/media/wsdl", prefix = "tr2")
class GetStreamUriResponse(

    @field:Element(name = "Uri", required = true)
    @param:Element(name = "Uri", required = true)
    @Namespace(reference = "http://www.onvif.org/ver20/media/wsdl", prefix = "tr2")
    var uri: String = ""
)

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s")
data class StreamMediaEnvelope(
    @field:Element(name = "Body", required = true)
    var body: StreamMediaBody
)

@Root(name = "Body", strict = false)
data class StreamMediaBody(
    @field:Element(name= "GetStreamUriResponse", required = false)
    var content: GetStreamMediaUriResponse
)
@Root(name = "GetStreamUriResponse", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/media/wsdl", prefix = "trt")
data class GetStreamMediaUriResponse(

    @field:Element(name = "MediaUri", required = true)
    @param:Element(name = "MediaUri", required = true)
    @Namespace(reference = "http://www.onvif.org/ver10/media/wsdl", prefix = "trt")
    var mediaUri: MediaUri = MediaUri()
)

@Root(name = "MediaUri", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/media/wsdl", prefix = "trt")
data class MediaUri(

    @field:Element(name = "Uri", required = true)
    @param:Element(name = "Uri", required = true)
    @Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
    var uri: String = "",

    @field:Element(name = "InvalidAfterConnect", required = true)
    @param:Element(name = "InvalidAfterConnect", required = true)
    @Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
    var invalidAfterConnect: Boolean = false,

    @field:Element(name = "InvalidAfterReboot", required = true)
    @param:Element(name = "InvalidAfterReboot", required = true)
    @Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
    var invalidAfterReboot: Boolean = false,

    @field:Element(name = "Timeout", required = true)
    @param:Element(name = "Timeout", required = true)
    @Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
    var timeout: String = ""
)
