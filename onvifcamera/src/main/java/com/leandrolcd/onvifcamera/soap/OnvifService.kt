package com.leandrolcd.onvifcamera.soap

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Root(name = "Service", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/device/wsdl", prefix = "tds")
class OnvifService(

    @field:Element(name = "Namespace", required = true)
    @param:Element(name = "Namespace", required = true)
    @Namespace(reference = "http://www.onvif.org/ver10/device/wsdl", prefix = "tds")
    var namespace: String = "",

    @field:Element(name = "XAddr", required = true)
    @param:Element(name = "XAddr", required = true)
    @Namespace(reference = "http://www.onvif.org/ver10/device/wsdl", prefix = "tds")
    var address: String = "",

    @field:Element(name = "Version", required = true)
    @param:Element(name = "Version", required = true)
    var version: Version = Version()
)
