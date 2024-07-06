package com.leandrolcd.onvifcamera.soap

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root


@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s")
data class ServiceInformationEnvelope(

    @field:Element(name = "Body", required = true)
    var body: ServiceInformationBody? = null
)

@Root(name = "Body", strict = false)
data class ServiceInformationBody(
    @field:Element(name = "GetDeviceInformationResponse", required = false)
    @field:Namespace(reference = "http://schemas.xmlsoap.org/ws/2005/04/discovery", prefix = "d")
    var service: GetDeviceInformationResponse? = null
)
@Root(name = "GetDeviceInformationResponse", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/device/wsdl", prefix = "tds")
class GetDeviceInformationResponse(

    @field:Element(name = "Manufacturer", required = true)
    @param:Element(name = "Manufacturer", required = true)
    @Namespace(reference = "http://www.onvif.org/ver10/device/wsdl", prefix = "tds")
    var manufacturer: String = "",

    @field:Element(name = "Model", required = true)
    @param:Element(name = "Model", required = true)
    @Namespace(reference = "http://www.onvif.org/ver10/device/wsdl", prefix = "tds")
    var model: String = "",

    @field:Element(name = "FirmwareVersion", required = true)
    @param:Element(name = "FirmwareVersion", required = true)
    @Namespace(reference = "http://www.onvif.org/ver10/device/wsdl", prefix = "tds")
    var firmwareVersion: String = "",

    @field:Element(name = "SerialNumber", required = true)
    @param:Element(name = "SerialNumber", required = true)
    @Namespace(reference = "http://www.onvif.org/ver10/device/wsdl", prefix = "tds")
    var serialNumber: String = "",

    @field:Element(name = "HardwareId", required = true)
    @param:Element(name = "HardwareId", required = true)
    @Namespace(reference = "http://www.onvif.org/ver10/device/wsdl", prefix = "tds")
    var hardwareId: String = ""
)
