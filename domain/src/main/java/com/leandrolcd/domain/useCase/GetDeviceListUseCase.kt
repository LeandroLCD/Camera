package com.leandrolcd.domain.useCase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetDeviceListUseCase @Inject constructor(private val repository: IDeviceRepository) {
    suspend operator  fun invoke() = repository.getDeviceList().flowOn(Dispatchers.IO)
}