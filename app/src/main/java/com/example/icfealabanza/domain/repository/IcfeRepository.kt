package com.example.icfealabanza.domain.repository

import com.example.icfealabanza.data.network.dto.album_from_artist.AlbumsFromArtistResponse
import com.example.icfealabanza.data.network.dto.artist_top_songs.ArtistsTopSongsResponse
import com.example.icfealabanza.data.network.dto.by_id.album_by_id.AlbumByIdDto
import com.example.icfealabanza.data.network.dto.by_id.artist_by_id.ArtistByIdDto
import com.example.icfealabanza.data.network.dto.related_artists.RelatedArtistsResponse
import com.example.icfealabanza.data.network.dto.search.search_album.SearchAlbumResponse
import com.example.icfealabanza.data.network.dto.search.search_artist.SearchArtistResponse
import com.example.icfealabanza.data.network.dto.search.search_song.SearchSongResponse
import com.example.icfealabanza.domain.models.Reunion
import com.example.icfealabanza.domain.models.SongListItem

interface IcfeRepository {

    suspend fun getAlbumById(
        id: String
    ): Result<AlbumByIdDto>


    suspend fun getArtistById(
        id: String
    ): Result<ArtistByIdDto>


    suspend fun getAlbumsFromArtist(
        id: String,
        limit: Int = 10,
        index: Int = 0
    ): Result<AlbumsFromArtistResponse>


    suspend fun getArtistsTopSongs(
        id: String,
        limit: Int = 5,
        index: Int = 0
    ): Result<ArtistsTopSongsResponse>


    suspend fun getRelatedArtists(
        id: String,
        limit: Int = 5,
        index: Int = 0
    ): Result<RelatedArtistsResponse>


    suspend fun searchArtist(
        query: String,
        limit: Int = 5,
        index: Int = 0
    ): Result<SearchArtistResponse>


    suspend fun searchAlbum(
        query: String,
        limit: Int = 5,
        index: Int = 0
    ): Result<SearchAlbumResponse>


    suspend fun searchSong(
        query: String,
        limit: Int = 5,
        index: Int = 0
    ): Result<SearchSongResponse>

    fun getReus(onSuccess: (List<Reunion>) -> Unit, onError: (Exception) -> Unit)

    suspend fun saveReu(reu: Reunion): Boolean

    fun getReuById(id: String, onSuccess: (Reunion) -> Unit, onError: (Exception) -> Unit)

    suspend fun deleteReu(id: String, onSuccess: (String) -> Unit, onError: (Exception) -> Unit)

    fun addTrackToReu(idReu: String, track: SongListItem, onErrorS: (String) -> Unit,
                      onError: (Exception) -> Unit, onSuccess: (String) -> Unit)

    fun deleteTrackFromReu(idReu: String, track: SongListItem, onSuccess: (String) -> Unit,
                           onErrorS: (String) -> Unit, onError: (Exception) -> Unit)

}