package com.leandrolcd.domain.models

data class Device(
    val id:Int,
    val serial:String,
    val alias:String,
    val canal:Int,
    val isConnected:Boolean = false,
    val snapshot:String? = null
)
