package com.example.icfealabanza.domain.use_cases

import android.util.Log
import com.example.icfealabanza.data.network.dto.search.search_artist.SearchArtistResponse
import com.example.icfealabanza.data.network.dto.search.search_artist.toListItem
import com.example.icfealabanza.data.repository.IcfeRepositoryImpl
import com.example.icfealabanza.domain.models.ArtistListItem
import javax.inject.Inject

class SearchArtistsUseCase @Inject constructor(private val repository: IcfeRepositoryImpl) {

    suspend operator fun invoke(query: String, limit: Int = 5, index: Int = 0): List<ArtistListItem> {
        val result = repository.searchArtist(query, limit, index)
        return if (result.isSuccess) result.getOrNull()!!.data.map { it.toListItem() }
        else emptyList()
    }
}