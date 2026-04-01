package com.leandrolcd.data.core.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leandrolcd.data.core.local.Entities.DeviceEntity

@Database(
    entities = [DeviceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CameraDataBase: RoomDatabase() {
    abstract fun cameraDao(): CameraDao
}