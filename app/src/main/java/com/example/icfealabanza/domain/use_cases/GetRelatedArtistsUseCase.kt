package com.example.icfealabanza.domain.use_cases

import com.example.icfealabanza.data.network.dto.related_artists.toListItem
import com.example.icfealabanza.data.repository.IcfeRepositoryImpl
import com.example.icfealabanza.domain.models.ArtistListItem
import javax.inject.Inject

class GetRelatedArtistsUseCase @Inject constructor(private val repositoryImpl: IcfeRepositoryImpl) {

    suspend operator fun invoke(id: String, limit: Int = 10, index: Int = 0): List<ArtistListItem>{
        val result = repositoryImpl.getRelatedArtists(id, limit, index)
        if (result.isSuccess) {
            return result.getOrNull()!!.data.map { it.toListItem() }
        }
        else return emptyList()
    }
}