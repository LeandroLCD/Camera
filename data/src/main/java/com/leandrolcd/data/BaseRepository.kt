package com.leandrolcd.data

import android.annotation.SuppressLint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

open class BaseRepository constructor(
    private var dispatchers: CoroutineDispatcher
) {
    @SuppressLint("DiscouragedApi")
    suspend fun <T> makeNetworkCall(
        call: suspend CoroutineScope.() -> T
    ): Result<T> {
        return withContext(dispatchers) {
            runCatching {
                call()
            }
        }
    }

}