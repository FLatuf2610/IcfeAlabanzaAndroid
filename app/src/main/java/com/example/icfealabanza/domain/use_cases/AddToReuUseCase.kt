package com.example.icfealabanza.domain.use_cases

import com.example.icfealabanza.data.repository.IcfeRepositoryImpl
import com.example.icfealabanza.domain.models.SongListItem
import javax.inject.Inject

class AddToReuUseCase @Inject constructor(private val icfeRepositoryImpl: IcfeRepositoryImpl) {
    operator fun invoke(
        idReu: String,
        track: SongListItem,
        onErrorS: (String) -> Unit,
        onError: (Exception) -> Unit,
        onSuccess: (String) -> Unit
    ) = icfeRepositoryImpl.addTrackToReu(idReu, track, onErrorS, onError, onSuccess)
}