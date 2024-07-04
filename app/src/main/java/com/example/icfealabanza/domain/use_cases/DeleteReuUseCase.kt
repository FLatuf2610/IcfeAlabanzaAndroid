package com.example.icfealabanza.domain.use_cases

import com.example.icfealabanza.data.repository.IcfeRepositoryImpl
import javax.inject.Inject

class DeleteReuUseCase @Inject constructor(private val repositoryImpl: IcfeRepositoryImpl) {
    suspend operator fun invoke(reuId: String, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) =
        repositoryImpl.deleteReu(id = reuId, onSuccess = onSuccess, onError = onError)
}