package com.example.icfealabanza.data.network.dto.artist_top_songs


import com.google.gson.annotations.SerializedName

data class ArtistsTopSongsResponse(
    @SerializedName("data")
    val data: List<ArtistsTopSongsSongDto>,
    @SerializedName("next")
    val next: String,
    @SerializedName("total")
    val total: Int
)