package com.leandrolcd.domain.useCase

import javax.inject.Inject

class GetDeviceByChannelUseCase @Inject constructor(private val repository: IDeviceRepository) {
    suspend operator fun invoke(id: Int) = repository.getDeviceByChannel(id)
}