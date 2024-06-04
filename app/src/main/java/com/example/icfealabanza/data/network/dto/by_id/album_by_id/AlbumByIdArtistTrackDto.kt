package com.example.icfealabanza.data.network.dto.by_id.album_by_id


import com.google.gson.annotations.SerializedName

data class AlbumByIdArtistTrackDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("tracklist")
    val tracklist: String,
    @SerializedName("type")
    val type: String
)