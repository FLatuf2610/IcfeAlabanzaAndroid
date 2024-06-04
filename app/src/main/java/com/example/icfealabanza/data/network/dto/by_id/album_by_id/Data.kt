package com.example.icfealabanza.data.network.dto.by_id.album_by_id


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("type")
    val type: String
)