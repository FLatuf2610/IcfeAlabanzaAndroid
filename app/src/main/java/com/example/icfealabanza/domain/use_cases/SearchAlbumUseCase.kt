package com.example.icfealabanza.domain.use_cases

import android.util.Log
import com.example.icfealabanza.data.network.dto.search.search_album.SearchAlbumResponse
import com.example.icfealabanza.data.network.dto.search.search_album.toItemList
import com.example.icfealabanza.data.repository.IcfeRepositoryImpl
import com.example.icfealabanza.domain.models.AlbumListItem
import javax.inject.Inject

class SearchAlbumUseCase @Inject constructor(private val repository: IcfeRepositoryImpl) {

    suspend operator fun invoke(query: String, limit: Int = 5, index: Int = 0): List<AlbumListItem> {
        val result = repository.searchAlbum(query, limit, index)
        return if (result.isSuccess) result.getOrNull()!!.data.map { it.toItemList() }
        else emptyList()
    }
}