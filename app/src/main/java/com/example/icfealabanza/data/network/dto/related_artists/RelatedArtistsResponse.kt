package com.example.icfealabanza.data.network.dto.related_artists


import com.google.gson.annotations.SerializedName

data class RelatedArtistsResponse(
    @SerializedName("data")
    val `data`: List<RelatedArtistsDto>,
    @SerializedName("total")
    val total: Int
)