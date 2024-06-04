package com.example.icfealabanza.data.network.dto.by_id.album_by_id


import com.google.gson.annotations.SerializedName

data class Tracks(
    @SerializedName("data")
    val data: List<AlbumByIdSongDto>
)