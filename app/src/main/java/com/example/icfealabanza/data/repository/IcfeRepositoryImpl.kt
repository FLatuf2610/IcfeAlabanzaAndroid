package com.example.icfealabanza.data.repository

import com.example.icfealabanza.data.network.api_client.DeezerApiClient
import com.example.icfealabanza.data.network.dto.album_from_artist.AlbumsFromArtistResponse
import com.example.icfealabanza.data.network.dto.artist_top_songs.ArtistsTopSongsResponse
import com.example.icfealabanza.data.network.dto.by_id.album_by_id.AlbumByIdDto
import com.example.icfealabanza.data.network.dto.by_id.artist_by_id.ArtistByIdDto
import com.example.icfealabanza.data.network.dto.by_id.song_by_id.SongByIdDto
import com.example.icfealabanza.data.network.dto.related_artists.RelatedArtistsResponse
import com.example.icfealabanza.data.network.dto.search.search_album.SearchAlbumResponse
import com.example.icfealabanza.data.network.dto.search.search_artist.SearchArtistResponse
import com.example.icfealabanza.data.network.dto.search.search_song.SearchSongResponse
import com.example.icfealabanza.domain.repository.IcfeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IcfeRepositoryImpl @Inject constructor(private val apiClient: DeezerApiClient) : IcfeRepository {

    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
        val result = try {
            val response = withContext(Dispatchers.IO) {
                apiCall()
            }
            if (response.isSuccessful && response.body() != null) Result.success(response.body()!!)
            else Result.failure(IOException(response.message()))
        } catch (e: Exception) {
            Result.failure(Exception(e))
        }
        return result
    }

    override suspend fun getAlbumById(id: String): Result<AlbumByIdDto> {
        return safeApiCall { apiClient.getAlbumById(id) }
    }

    override suspend fun getSongById(id: String): Result<SongByIdDto> {
        return safeApiCall { apiClient.getSongById(id) }
    }

    override suspend fun getArtistById(id: String): Result<ArtistByIdDto> {
        return safeApiCall { apiClient.getArtistById(id) }
    }

    override suspend fun getAlbumsFromArtist(id: String): Result<AlbumsFromArtistResponse> {
        return safeApiCall { apiClient.getAlbumsFromArtist(id) }
    }

    override suspend fun getArtistsTopSongs(
        id: String,
        limit: Int,
        index: Int
    ): Result<ArtistsTopSongsResponse> {
        return safeApiCall { apiClient.getArtistsTopSongs(id, limit, index) }
    }

    override suspend fun getRelatedArtists(
        id: String,
        limit: Int,
        index: Int
    ): Result<RelatedArtistsResponse> {
        return safeApiCall { apiClient.getRelatedArtists(id, limit, index) }
    }

    override suspend fun searchArtist(
        query: String,
        limit: Int,
        index: Int
    ): Result<SearchArtistResponse> {
        return safeApiCall { apiClient.searchArtist(query, limit, index) }
    }

    override suspend fun searchAlbum(
        query: String,
        limit: Int,
        index: Int
    ): Result<SearchAlbumResponse> {
        return safeApiCall { apiClient.searchAlbum(query, limit, index) }
    }

    override suspend fun searchSong(
        query: String,
        limit: Int,
        index: Int
    ): Result<SearchSongResponse> {
        return safeApiCall { apiClient.searchSong(query, limit, index) }

    }
}