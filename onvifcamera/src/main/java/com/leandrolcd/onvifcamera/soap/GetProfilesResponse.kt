package com.leandrolcd.onvifcamera.soap

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root
// ============================
// SOAP Envelope / Body
// ============================
@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s")
data class ProfileEnvelope(
    @field:Element(name = "Body", required = true)
    var body: ProfileBody? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s")
data class ProfileBody(
    // <trt:GetProfilesResponse> ...
    @field:Element(name = "GetProfilesResponse", required = false)
    @field:Namespace(reference = "http://www.onvif.org/ver10/media/wsdl", prefix = "trt")
    var content: GetProfilesResponse? = null
)

// ============================
// trt:GetProfilesResponse
// ============================
@Root(name = "GetProfilesResponse", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/media/wsdl", prefix = "trt")
data class GetProfilesResponse(
    @field:ElementList(entry = "Profiles", inline = true, required = false, empty = true)
    var profiles: List<Profile> = emptyList()
)

@Root(name = "Profiles", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/media/wsdl", prefix = "trt")
data class Profile(

    @field:Attribute(name = "token", required = false)
    var token: String? = null,

    @field:Attribute(name = "fixed", required = false)
    var fixed: Boolean? = null,

    @field:Element(name = "Name", required = false)
    @field:Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
    var name: String? = null,

    @field:Element(name = "VideoSourceConfiguration", required = false)
    var videoSourceConfig: VideoSourceConfiguration? = null,

    @field:Element(name = "AudioSourceConfiguration", required = false)
    var audioSourceConfig: AudioSourceConfiguration? = null,

    @field:Element(name = "VideoEncoderConfiguration", required = false)
    var videoEncoderConfig: VideoEncoderConfiguration? = null,

    @field:Element(name = "AudioEncoderConfiguration", required = false)
    var audioEncoderConfig: AudioEncoderConfiguration? = null,

    @field:Element(name = "PTZConfiguration", required = false)
    var ptzConfig: PTZConfiguration? = null
)

// ============================
// tt:VideoSourceConfiguration
// ============================
@Root(name = "VideoSourceConfiguration", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
data class VideoSourceConfiguration(
    @field:Attribute(name = "token", required = false)
    var token: String? = null,

    @field:Element(name = "Name", required = false)
    var name: String? = null,

    @field:Element(name = "UseCount", required = false)
    var useCount: Int? = null,

    @field:Element(name = "SourceToken", required = false)
    var sourceToken: String? = null,

    @field:Element(name = "Bounds", required = false)
    var bounds: Bounds? = null
)

@Root(name = "Bounds", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
data class Bounds(
    @field:Attribute(name = "x", required = false) var x: Int? = null,
    @field:Attribute(name = "y", required = false) var y: Int? = null,
    @field:Attribute(name = "width", required = false) var width: Int? = null,
    @field:Attribute(name = "height", required = false) var height: Int? = null
)

// ============================
// tt:AudioSourceConfiguration
// ============================
@Root(name = "AudioSourceConfiguration", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
data class AudioSourceConfiguration(
    @field:Attribute(name = "token", required = false)
    var token: String? = null,

    @field:Element(name = "Name", required = false)
    var name: String? = null,

    @field:Element(name = "UseCount", required = false)
    var useCount: Int? = null,

    @field:Element(name = "SourceToken", required = false)
    var sourceToken: String? = null
)

// ============================
// tt:VideoEncoderConfiguration (+ hijos RateControl, H264, Multicast)
// ============================
@Root(name = "VideoEncoderConfiguration", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
data class VideoEncoderConfiguration(
    @field:Attribute(name = "token", required = false)
    var token: String? = null,

    @field:Element(name = "Name", required = false)
    var name: String? = null,

    @field:Element(name = "UseCount", required = false)
    var useCount: Int? = null,

    @field:Element(name = "Encoding", required = false)
    var encoding: String? = null,

    @field:Element(name = "Resolution", required = false)
    var resolution: Resolution? = null,

    @field:Element(name = "Quality", required = false)
    var quality: Int? = null,

    @field:Element(name = "RateControl", required = false)
    var rateControl: RateControl? = null,

    @field:Element(name = "H264", required = false)
    var h264: H264? = null,

    @field:Element(name = "Multicast", required = false)
    var multicast: Multicast? = null,

    @field:Element(name = "SessionTimeout", required = false)
    var sessionTimeout: String? = null
)

@Root(name = "Resolution", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
data class Resolution(
    @field:Element(name = "Width", required = false) var width: Int? = null,
    @field:Element(name = "Height", required = false) var height: Int? = null
)

@Root(name = "RateControl", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
data class RateControl(
    @field:Element(name = "FrameRateLimit", required = false) var frameRateLimit: Int? = null,
    @field:Element(name = "EncodingInterval", required = false) var encodingInterval: Int? = null,
    @field:Element(name = "BitrateLimit", required = false) var bitrateLimit: Int? = null
)

@Root(name = "H264", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
data class H264(
    @field:Element(name = "GovLength", required = false) var govLength: Int? = null,
    @field:Element(name = "H264Profile", required = false) var h264Profile: String? = null
)

@Root(name = "Multicast", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
data class Multicast(
    @field:Element(name = "Address", required = false) var address: IPAddress? = null,
    @field:Element(name = "Port", required = false) var port: Int? = null,
    @field:Element(name = "TTL", required = false) var ttl: Int? = null,
    @field:Element(name = "AutoStart", required = false) var autoStart: Boolean? = null
)

@Root(name = "Address", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
data class IPAddress(
    @field:Element(name = "Type", required = false) var type: String? = null,          // IPv4 / IPv6
    @field:Element(name = "IPv4Address", required = false) var ipv4: String? = null
)

// ============================
// tt:AudioEncoderConfiguration (+ Multicast)
// ============================
@Root(name = "AudioEncoderConfiguration", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
data class AudioEncoderConfiguration(
    @field:Attribute(name = "token", required = false)
    var token: String? = null,

    @field:Element(name = "Name", required = false)
    var name: String? = null,

    @field:Element(name = "UseCount", required = false)
    var useCount: Int? = null,

    @field:Element(name = "Encoding", required = false)
    var encoding: String? = null,

    @field:Element(name = "Bitrate", required = false)
    var bitrate: Int? = null,

    @field:Element(name = "SampleRate", required = false)
    var sampleRate: Int? = null,

    @field:Element(name = "Multicast", required = false)
    var multicast: Multicast? = null,

    @field:Element(name = "SessionTimeout", required = false)
    var sessionTimeout: String? = null
)

// ============================
// tt:PTZConfiguration (campos presentes en tu XML)
// ============================
@Root(name = "PTZConfiguration", strict = false)
@Namespace(reference = "http://www.onvif.org/ver10/schema", prefix = "tt")
data class PTZConfiguration(
    @field:Attribute(name = "token", required = false)
    var token: String? = null,

    @field:Element(name = "Name", required = false)
    var name: String? = null,

    @field:Element(name = "UseCount", required = false)
    var useCount: Int? = null,

    @field:Element(name = "NodeToken", required = false)
    var nodeToken: String? = null,

    // Nota: en tu XML aparece "DefaultAbsolutePantTiltPositionSpace" (con 'PantTilt').
    // Si fuese "PanTilt" en otros dispositivos, puedes duplicar el campo con alias si hace falta.
    @field:Element(name = "DefaultAbsolutePantTiltPositionSpace", required = false)
    var defaultAbsPanTiltPositionSpace: String? = null,

    @field:Element(name = "DefaultAbsoluteZoomPositionSpace", required = false)
    var defaultAbsZoomPositionSpace: String? = null,

    @field:Element(name = "DefaultRelativePanTiltTranslationSpace", required = false)
    var defaultRelPanTiltTranslationSpace: String? = null,

    @field:Element(name = "DefaultRelativeZoomTranslationSpace", required = false)
    var defaultRelZoomTranslationSpace: String? = null,

    @field:Element(name = "DefaultContinuousPanTiltVelocitySpace", required = false)
    var defaultContPanTiltVelocitySpace: String? = null
)