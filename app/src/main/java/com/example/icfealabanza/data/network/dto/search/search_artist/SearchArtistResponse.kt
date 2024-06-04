package com.example.icfealabanza.data.network.dto.search.search_artist


import com.google.gson.annotations.SerializedName

data class SearchArtistResponse(
    @SerializedName("data")
    val data: List<ArtistDto>,
    @SerializedName("next")
    val next: String,
    @SerializedName("total")
    val total: Int
)