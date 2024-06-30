package com.leandrolcd.domain.models

data class Device(
    val id:Int,
    val canal:Int,
    val snapshot:String? = null)
