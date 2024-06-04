package com.example.icfealabanza.data.network.dto.artist_top_songs


import com.google.gson.annotations.SerializedName

data class ArtistsTopSongsArtistDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("tracklist")
    val tracklist: String,
    @SerializedName("type")
    val type: String
)