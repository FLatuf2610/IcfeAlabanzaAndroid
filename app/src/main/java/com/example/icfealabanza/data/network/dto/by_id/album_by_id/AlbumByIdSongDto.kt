package com.example.icfealabanza.data.network.dto.by_id.album_by_id


import com.example.icfealabanza.domain.models.SongListItem
import com.google.gson.annotations.SerializedName

data class AlbumByIdSongDto(
    @SerializedName("album")
    val album: Album,
    @SerializedName("artist")
    val artist: AlbumByIdArtistTrackDto,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("explicit_content_cover")
    val explicitContentCover: Int,
    @SerializedName("explicit_content_lyrics")
    val explicitContentLyrics: Int,
    @SerializedName("explicit_lyrics")
    val explicitLyrics: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("md5_image")
    val md5Image: String,
    @SerializedName("preview")
    val preview: String,
    @SerializedName("rank")
    val rank: String,
    @SerializedName("readable")
    val readable: Boolean,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_short")
    val titleShort: String,
    @SerializedName("title_version")
    val titleVersion: String,
    @SerializedName("type")
    val type: String
)
fun AlbumByIdSongDto.toListItem(): SongListItem {
    return SongListItem(id, title, album.coverMedium, artist.name, preview)
}