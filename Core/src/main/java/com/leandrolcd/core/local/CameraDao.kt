package com.leandrolcd.core.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.leandrolcd.core.local.Entities.DeviceEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CameraDao {

    @Query("SELECT * FROM device")
    fun getDevices(): Flow<List<DeviceEntity>>

    @Query("SELECT * FROM device WHERE id = :id")
    fun getDevice(id:Int): DeviceEntity?

    @Query("SELECT * FROM device WHERE id = :id")
    fun getDeviceFlow(id:Int): Flow<DeviceEntity>

    @Query("SELECT * FROM device WHERE canal = :id")
    fun getDeviceByChannel(id:Int): DeviceEntity?

    @Insert
    fun save(device: DeviceEntity)

    @Update(onConflict =  OnConflictStrategy.REPLACE)
    fun updateDevice(device: DeviceEntity)


    @Query("DELETE FROM device WHERE id = :id")
    fun delete(id:Int):Int
}