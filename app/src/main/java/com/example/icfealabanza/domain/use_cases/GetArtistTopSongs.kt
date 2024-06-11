package com.example.icfealabanza.domain.use_cases

import com.example.icfealabanza.data.network.dto.artist_top_songs.ArtistsTopSongsSongDto
import com.example.icfealabanza.data.network.dto.artist_top_songs.toListItem
import com.example.icfealabanza.data.repository.IcfeRepositoryImpl
import com.example.icfealabanza.domain.models.SongListItem
import javax.inject.Inject

class GetArtistTopSongs @Inject constructor(private val repositoryImpl: IcfeRepositoryImpl){

    suspend operator fun invoke(id: String, limit: Int = 5, index: Int = 0): List<SongListItem> {
        val result = repositoryImpl.getArtistsTopSongs(id, limit, index)
        if (result.isSuccess) {
            return result.getOrNull()!!.data.map { it.toListItem() }
        }
        else return emptyList()
    }
}

