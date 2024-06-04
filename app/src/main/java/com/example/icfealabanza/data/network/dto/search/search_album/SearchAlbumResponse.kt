package com.example.icfealabanza.data.network.dto.search.search_album


import com.google.gson.annotations.SerializedName

data class SearchAlbumResponse(
    @SerializedName("data")
    val data: List<AlbumDto>,
    @SerializedName("total")
    val total: Int
)