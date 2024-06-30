package com.leandrolcd.domain.useCase
import com.leandrolcd.domain.models.ResultTypeUiState
import java.net.URI
import javax.inject.Inject

class GetSnapshotUrlUseCase @Inject constructor(private val repository: IOnvifDeviceRepository) {
    suspend operator fun invoke(serial:String): ResultTypeUiState<URI> {
        return if(serial.isBlank()){
            ResultTypeUiState.Error("Serial Invalid")
        }else{
            repository.getSnapshotUrl(serial)
        }

    }
}