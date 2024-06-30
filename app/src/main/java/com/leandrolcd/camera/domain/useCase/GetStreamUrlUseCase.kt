package com.leandrolcd.camera.domain.useCase

import com.leandrolcd.camera.data.models.ResultType
import java.net.URI
import javax.inject.Inject

class GetStreamUrlUseCase  @Inject constructor(private val repository:IOnvifDeviceRepository) {
    suspend operator fun invoke(serial:String): ResultType<URI> {
       return if(serial.isBlank()){
            ResultType.Error("Serial Invalid")
        }else{
            repository.getStreamUrl(serial)
        }

    }
}