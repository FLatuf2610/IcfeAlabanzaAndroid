package com.example.icfealabanza.domain.use_cases

import com.example.icfealabanza.data.network.dto.album_from_artist.toListItem
import com.example.icfealabanza.data.repository.IcfeRepositoryImpl
import com.example.icfealabanza.domain.models.AlbumListItem
import javax.inject.Inject

class GetAlbumsFromArtist @Inject constructor(private val repositoryImpl: IcfeRepositoryImpl) {

    suspend operator fun invoke(id: String, limit: Int, index: Int): List<AlbumListItem> {
        val result = repositoryImpl.getAlbumsFromArtist(id, limit, index)
        return if (result.isSuccess) {
            result.getOrNull()!!.data.map { it.toListItem() }
        } else emptyList()
    }
}