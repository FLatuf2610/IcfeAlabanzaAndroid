package com.example.icfealabanza.domain.use_cases

import com.example.icfealabanza.data.network.dto.by_id.artist_by_id.toListItem
import com.example.icfealabanza.data.repository.IcfeRepositoryImpl
import com.example.icfealabanza.domain.models.ArtistListItem
import javax.inject.Inject

class GetArtistByIdUseCase @Inject constructor(private val repositoryImpl: IcfeRepositoryImpl) {

    suspend operator fun invoke(id: String): ArtistListItem? {
        val result = repositoryImpl.getArtistById(id)
        if (result.isSuccess) {
            return result.getOrNull()!!.toListItem()
        }
        else return null
    }
}