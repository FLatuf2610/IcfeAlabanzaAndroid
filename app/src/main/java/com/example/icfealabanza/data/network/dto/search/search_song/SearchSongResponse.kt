package com.example.icfealabanza.data.network.dto.search.search_song


import com.google.gson.annotations.SerializedName

data class SearchSongResponse(
    @SerializedName("data")
    val `data`: List<SongDto>,
    @SerializedName("next")
    val next: String,
    @SerializedName("total")
    val total: Int
)