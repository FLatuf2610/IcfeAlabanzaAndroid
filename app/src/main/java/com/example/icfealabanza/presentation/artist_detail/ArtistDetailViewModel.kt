package com.example.icfealabanza.presentation.artist_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icfealabanza.domain.models.AlbumListItem
import com.example.icfealabanza.domain.models.ArtistListItem
import com.example.icfealabanza.domain.models.SongListItem
import com.example.icfealabanza.domain.use_cases.GetAlbumsFromArtist
import com.example.icfealabanza.domain.use_cases.GetArtistByIdUseCase
import com.example.icfealabanza.domain.use_cases.GetArtistTopSongs
import com.example.icfealabanza.domain.use_cases.GetRelatedArtistsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val getArtistTopSongs: GetArtistTopSongs,
    private val getRelatedArtistsUseCase: GetRelatedArtistsUseCase,
    private val getArtistByIdUseCase: GetArtistByIdUseCase,
    private val getAlbumsFromArtist: GetAlbumsFromArtist
) : ViewModel() {

    var artist = MutableStateFlow<ArtistListItem?>(null)
        private set
    var artistTopSongs = MutableStateFlow<List<SongListItem>>(emptyList())
        private set
    var artistTopSongsLoading by mutableStateOf(false)
    var relatedArtists = MutableStateFlow<List<ArtistListItem>>(emptyList())
        private set
    var artistAlbums = MutableStateFlow<List<AlbumListItem>>(emptyList())
        private set
    var artistAlbumsLoading by mutableStateOf(false)

    var isTopSongsSheetShowed by mutableStateOf(false)


    fun initViewModel(artistId: String) {
        viewModelScope.launch {
            getTopSongs(id = artistId, limit = 15)
            getRelatedArtists(id = artistId)
            getArtist(artistId)
            getArtistAlbums(artistId)
        }
    }

    private suspend fun getArtist(id: String) {
        artist.value = getArtistByIdUseCase(id)
    }

    suspend fun getTopSongs(id: String, limit: Int = 5,index: Int = 0) {
        val topSongs = getArtistTopSongs(id = id, limit = limit,index = index)
        if (index != 0) {
            val aux = artistTopSongs.value.toMutableList()
            aux.addAll(topSongs)
            artistTopSongs.value = aux
        }
        else artistTopSongs.value = topSongs
    }

    suspend fun getRelatedArtists(id: String, index: Int = 0) {
        relatedArtists.value = getRelatedArtistsUseCase(id, index)
    }

    suspend fun getArtistAlbums(id:String, limit: Int = 10, index: Int = 0) {
        val albums = getAlbumsFromArtist(id, limit, index)
        if (index != 0) {
            val aux = artistAlbums.value.toMutableList()
            aux.addAll(albums)
            artistAlbums.value = aux
        }
        else artistAlbums.value = albums
    }

}