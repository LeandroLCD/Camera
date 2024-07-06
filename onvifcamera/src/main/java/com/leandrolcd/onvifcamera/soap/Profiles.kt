package com.leandrolcd.onvifcamera.soap

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Root(name = "Profiles", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/media/wsdl", prefix = "trt")
class Profiles(

    @field:Attribute(name = "token", required = true)
    @param:Attribute(name = "token", required = true)
    var token: String = "",

    @field:Element(name = "Name", required = true)
    @param:Element(name = "Name", required = true)
    @Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
    var name: String = "",

    @field:Element(name = "VideoEncoderConfiguration", required = true)
    @param:Element(name = "VideoEncoderConfiguration", required = true)
    var encoder: VideoEncoderConfiguration = VideoEncoderConfiguration()
)
