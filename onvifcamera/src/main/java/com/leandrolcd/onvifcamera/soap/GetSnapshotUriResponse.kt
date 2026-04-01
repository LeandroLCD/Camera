package com.leandrolcd.onvifcamera.soap

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root


@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s")
data class SnapshotEnvelope(
    @field:Element(name = "Body", required = true)
    var body: SnapshotBody
)

@Root(name = "Body", strict = false)
data class SnapshotBody(
    @field:Element(name= "GetSnapshotUriResponse", required = false)
    var content: GetStreamMediaUriResponse
)

@Root(name = "GetSnapshotUriResponse", strict = false)
@Namespace(reference = "http://www.onvif.org/ver20/media/wsdl", prefix = "tr2")
class GetSnapshotUriResponse(

    @field:Element(name = "Uri", required = true)
    @param:Element(name = "Uri", required = true)
    var uri: String = ""
)
