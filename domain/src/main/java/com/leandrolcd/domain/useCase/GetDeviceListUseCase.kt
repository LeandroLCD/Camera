package com.leandrolcd.domain.useCase

import javax.inject.Inject

class GetDeviceListUseCase @Inject constructor(private val repository: IDeviceRepository) {
    operator  fun invoke() = repository.getDeviceList()
}