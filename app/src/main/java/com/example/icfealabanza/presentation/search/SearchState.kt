package com.example.icfealabanza.presentation.search

import com.example.icfealabanza.domain.models.AlbumListItem
import com.example.icfealabanza.domain.models.ArtistListItem
import com.example.icfealabanza.domain.models.SongListItem


data class SearchState(
    val albums: MutableList<AlbumListItem> = mutableListOf(),
    val artists: MutableList<ArtistListItem> = mutableListOf(),
    val songs: MutableList<SongListItem> = mutableListOf(),
)