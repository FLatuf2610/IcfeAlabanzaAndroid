package com.example.icfealabanza.data.network.dto.by_id.song_by_id


import com.google.gson.annotations.SerializedName

data class SongByIdArtistDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("picture_big")
    val pictureBig: String,
    @SerializedName("picture_medium")
    val pictureMedium: String,
    @SerializedName("picture_small")
    val pictureSmall: String,
    @SerializedName("picture_xl")
    val pictureXl: String,
    @SerializedName("radio")
    val radio: Boolean,
    @SerializedName("share")
    val share: String,
    @SerializedName("tracklist")
    val tracklist: String,
    @SerializedName("type")
    val type: String
)