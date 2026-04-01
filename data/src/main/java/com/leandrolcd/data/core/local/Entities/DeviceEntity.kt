package com.leandrolcd.data.core.local.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device")
class DeviceEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val serial:String,
    val alias:String,
    val canal:Int,
    val user:String,
    val password:String,
    val url:String,
    val snapshot:String? = null
)