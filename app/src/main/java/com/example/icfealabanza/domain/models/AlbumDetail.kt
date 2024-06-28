package com.example.icfealabanza.domain.models

data class AlbumDetail(
    val id: String,
    val cover: String,
    val title: String,
    val tracks: List<SongListItem>
)