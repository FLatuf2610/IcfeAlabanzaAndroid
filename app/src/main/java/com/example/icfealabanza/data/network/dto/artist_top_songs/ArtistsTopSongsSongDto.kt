package com.example.icfealabanza.data.network.dto.artist_top_songs


import com.example.icfealabanza.domain.models.SongListItem
import com.google.gson.annotations.SerializedName

data class ArtistsTopSongsSongDto(
    @SerializedName("album")
    val album: TopSongsAlbumDto,
    @SerializedName("artist")
    val artist: ArtistsTopSongsArtistDto,
    @SerializedName("contributors")
    val contributors: List<ArtistsTopSongsContributorDto>,
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
fun ArtistsTopSongsSongDto.toListItem(): SongListItem {
    return SongListItem(id, title, album.coverMedium, artist.name, preview)
}