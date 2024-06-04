package com.example.icfealabanza.data.network.api_client

import com.example.icfealabanza.data.network.dto.album_from_artist.AlbumsFromArtistResponse
import com.example.icfealabanza.data.network.dto.artist_top_songs.ArtistsTopSongsResponse
import com.example.icfealabanza.data.network.dto.by_id.album_by_id.AlbumByIdDto
import com.example.icfealabanza.data.network.dto.by_id.artist_by_id.ArtistByIdDto
import com.example.icfealabanza.data.network.dto.search.search_artist.SearchArtistResponse
import com.example.icfealabanza.data.network.dto.search.search_song.SearchSongResponse
import com.example.icfealabanza.data.network.dto.by_id.song_by_id.SongByIdDto
import com.example.icfealabanza.data.network.dto.related_artists.RelatedArtistsResponse
import com.example.icfealabanza.data.network.dto.search.search_album.SearchAlbumResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeezerApiClient {
    @GET("album/{id}")
    suspend fun getAlbumById(
        @Path("id") id: String
    ): Response<AlbumByIdDto>

    @GET("track/{id}")
    suspend fun getSongById(
        @Path("id") id: String
    ): Response<SongByIdDto>

    @GET("artist/{id}")
    suspend fun getArtistById(
        @Path("id") id: String
    ): Response<ArtistByIdDto>

    @GET("artist/{id}/albums")
    suspend fun getAlbumsFromArtist(
        @Path("id")id: String
    ): Response<AlbumsFromArtistResponse>

    @GET("artist/{id}/top")
    suspend fun getArtistsTopSongs(
        @Path("id")id: String,
        @Query("limit")limit: Int = 5,
        @Query("index")index: Int = 0
    ): Response<ArtistsTopSongsResponse>

    @GET("artist/{id}/related")
    suspend fun getRelatedArtists(
        @Path("id")id: String,
        @Query("limit")limit: Int = 5,
        @Query("index")index: Int = 0
    ): Response<RelatedArtistsResponse>

    @GET("search/artist")
    suspend fun searchArtist(
        @Query("q") query: String,
        @Query("limit")limit: Int = 5,
        @Query("index")index: Int = 0
    ): Response<SearchArtistResponse>

    @GET("search/album")
    suspend fun searchAlbum(
    @Query("q") query: String,
    @Query("limit")limit: Int = 5,
    @Query("index")index: Int = 0
    ): Response<SearchAlbumResponse>

    @GET("search/track")
    suspend fun searchSong(
        @Query("q") query: String,
        @Query("limit")limit: Int = 5,
        @Query("index")index: Int = 0
    ): Response<SearchSongResponse>
}