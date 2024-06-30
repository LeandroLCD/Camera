package com.leandrolcd.camera.core.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.leandrolcd.camera.core.local.Entities.DeviceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CameraDao {

    @Query("SELECT * FROM device")
    suspend fun getDevices(): Flow<DeviceEntity>

    @Query("SELECT * FROM device WHERE id = :id")
    fun getDevice(id:Int):DeviceEntity?

    @Insert
    fun save(device: DeviceEntity):Int

    @Update(onConflict =  OnConflictStrategy.REPLACE)
    fun updateDevice(device: DeviceEntity)


    @Query("DELETE FROM device WHERE id = :id")
    fun delete(id:Int):Int
}