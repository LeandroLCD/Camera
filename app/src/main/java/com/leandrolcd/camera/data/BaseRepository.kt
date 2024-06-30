package com.leandrolcd.camera.data

import android.annotation.SuppressLint
import com.leandrolcd.camera.data.models.ResultType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

open class BaseRepository constructor(
    private var dispatchers: CoroutineDispatcher
) {
    @SuppressLint("DiscouragedApi")
    suspend fun <T> makeNetworkCall(
        call: suspend CoroutineScope.() -> T
    ): ResultType<T> {
        return withContext(dispatchers) {
            try {
                ResultType.Success(call())
            }
            catch (e: Exception) {
                return@withContext ResultType.Error(e.message.orEmpty())

            }

        }
    }

}