package com.example.icfealabanza.domain.use_cases

import com.example.icfealabanza.data.repository.IcfeRepositoryImpl
import com.example.icfealabanza.domain.models.SongListItem
import javax.inject.Inject

class DeleteTrackFromReuUseCase @Inject constructor(private val repositoryImpl: IcfeRepositoryImpl) {
    operator fun invoke(reuId: String, onSuccess: (String) -> Unit, onError: (Exception) -> Unit,
                        onErrorS: (String) -> Unit, track: SongListItem) =
        repositoryImpl.deleteTrackFromReu(
            idReu = reuId,
            onErrorS = onErrorS,
            onError = onError,
            onSuccess = onSuccess,
            track = track
        )
}