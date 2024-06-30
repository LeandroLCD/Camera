package com.leandrolcd.camera.domain.useCase

import com.leandrolcd.camera.core.di.DispatchersIO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetDeviceListUseCase @Inject constructor(private val repository: IDeviceRepository,
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher) {
    suspend operator  fun invoke() = repository.getDeviceList().flowOn(dispatchersIO)
}