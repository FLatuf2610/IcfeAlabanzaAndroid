package com.example.icfealabanza.domain.models

data class Reunion(
    var id: String? = null,
    val name: String = "",
    val tracks: MutableList<SongListItem> = mutableListOf()
)
