package com.example.icfealabanza.domain.use_cases

import com.example.icfealabanza.data.repository.IcfeRepositoryImpl
import com.example.icfealabanza.domain.models.Reunion
import javax.inject.Inject

class GetReusUseCase @Inject constructor(private val repositoryImpl: IcfeRepositoryImpl) {

    operator fun invoke(onSuccess: (List<Reunion>) -> Unit, onError: (Exception) -> Unit) =
        repositoryImpl.getReus(onSuccess, onError)
}