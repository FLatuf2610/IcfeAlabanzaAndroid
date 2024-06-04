package com.example.icfealabanza.domain.use_cases

import android.util.Log
import com.example.icfealabanza.data.network.dto.search.search_song.SearchSongResponse
import com.example.icfealabanza.data.network.dto.search.search_song.toListItem
import com.example.icfealabanza.data.repository.IcfeRepositoryImpl
import com.example.icfealabanza.domain.models.SongListItem
import javax.inject.Inject

class SearchSongsUseCase @Inject constructor(private val repository: IcfeRepositoryImpl) {

    suspend operator fun invoke(query: String, limit: Int = 5, index: Int = 0): List<SongListItem> {
        val result = repository.searchSong(query, limit, index)
        return if (result.isSuccess) result.getOrNull()!!.data.map { it.toListItem() }
        else emptyList()
    }
}