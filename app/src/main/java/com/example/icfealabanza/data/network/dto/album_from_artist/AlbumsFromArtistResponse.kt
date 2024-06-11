package com.example.icfealabanza.data.network.dto.album_from_artist


import com.google.gson.annotations.SerializedName

data class AlbumsFromArtistResponse(
    @SerializedName("data")
    val data: List<AlbumDto>,
    @SerializedName("next")
    val next: String,
    @SerializedName("total")
    val total: Int
)