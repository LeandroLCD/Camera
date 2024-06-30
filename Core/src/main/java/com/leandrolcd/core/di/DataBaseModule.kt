package com.leandrolcd.core.di

import android.content.Context
import androidx.room.Room
import com.leandrolcd.core.local.CameraDao
import com.leandrolcd.core.local.CameraDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Provides
    fun provideCameraDao(database: CameraDataBase): CameraDao {
        return database.cameraDao()
    }

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext appContext: Context): CameraDataBase {
        return Room.databaseBuilder(appContext, CameraDataBase::class.java, "Database")
            .fallbackToDestructiveMigration()
            .build()
    }
}