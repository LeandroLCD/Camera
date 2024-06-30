package com.leandrolcd.camera.core.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.leandrolcd.camera.core.local.Entities.DeviceEntity

@Database(
    entities = [DeviceEntity::class],
    version = 1
)
abstract class CameraDataBase: RoomDatabase() {
    abstract fun cameraDao():CameraDao
}