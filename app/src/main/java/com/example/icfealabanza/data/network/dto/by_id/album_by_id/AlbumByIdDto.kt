package com.example.icfealabanza.data.network.dto.by_id.album_by_id


import com.example.icfealabanza.domain.models.AlbumDetail
import com.google.gson.annotations.SerializedName

data class AlbumByIdDto(
    @SerializedName("artist")
    val artist: AlbumByIdArtistDto,
    @SerializedName("available")
    val available: Boolean,
    @SerializedName("contributors")
    val contributors: List<AlbumByIdContributorDto>,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("cover_big")
    val coverBig: String,
    @SerializedName("cover_medium")
    val coverMedium: String,
    @SerializedName("cover_small")
    val coverSmall: String,
    @SerializedName("cover_xl")
    val coverXl: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("explicit_content_cover")
    val explicitContentCover: Int,
    @SerializedName("explicit_content_lyrics")
    val explicitContentLyrics: Int,
    @SerializedName("explicit_lyrics")
    val explicitLyrics: Boolean,
    @SerializedName("fans")
    val fans: Int,
    @SerializedName("genre_id")
    val genreId: Int,
    @SerializedName("genres")
    val genres: AlbumByIdListGenresDto,
    @SerializedName("id")
    val id: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("md5_image")
    val md5Image: String,
    @SerializedName("nb_tracks")
    val nbTracks: Int,
    @SerializedName("record_type")
    val recordType: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("share")
    val share: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("tracklist")
    val tracklist: String,
    @SerializedName("tracks")
    val tracks: Tracks,
    @SerializedName("type")
    val type: String,
    @SerializedName("upc")
    val upc: String
)
fun AlbumByIdDto.toDomain(): AlbumDetail {
    val trackList = tracks.data.map { it.toListItem() }
    return AlbumDetail(
        id = id,
        cover = coverBig,
        title = title,
        tracks = trackList
    )
}
