package com.leandrolcd.domain.useCase

import javax.inject.Inject

class GetDeviceByIdUseCase @Inject constructor(private val repository: IDeviceRepository) {
    operator  fun invoke(id:Int) = repository.getDeviceById(id)
}