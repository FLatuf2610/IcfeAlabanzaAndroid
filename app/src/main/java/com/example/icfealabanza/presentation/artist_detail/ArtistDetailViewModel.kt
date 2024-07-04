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
import kotlinx.coroutines.async
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

    private var artistId = ""
    var artist = MutableStateFlow<ArtistListItem?>(null)
        private set
    var isLoadingState by mutableStateOf(false)
    var artistTopSongs = MutableStateFlow<List<SongListItem>>(emptyList())
        private set
    var relatedArtists = MutableStateFlow<List<ArtistListItem>>(emptyList())
        private set
    var artistAlbums = MutableStateFlow<List<AlbumListItem>>(emptyList())
        private set
    var artistAlbumsLoading by mutableStateOf(false)
        private set



    fun initViewModel(artistId: String) {
        if (artistId == this.artistId) return
        this.artistId = artistId
        viewModelScope.launch {
            isLoadingState = true
            async { getTopSongs(id = artistId, limit = 15)
                getRelatedArtists(id = artistId)
                getArtist(artistId)
                getArtistAlbums(artistId) }.await()
            isLoadingState = false
        }
    }

    private suspend fun getArtist(id: String) {
        artist.value = getArtistByIdUseCase(id)
    }

    private suspend fun getTopSongs(id: String, limit: Int = 5,index: Int = 0) {
        val topSongs = getArtistTopSongs(id = id, limit = limit,index = index)
        if (index != 0) {
            val aux = artistTopSongs.value.toMutableList()
            aux.addAll(topSongs)
            artistTopSongs.value = aux
        }
        else artistTopSongs.value = topSongs
    }

    private fun getRelatedArtists(id: String, index: Int = 0) {
        viewModelScope.launch {
            val result = getRelatedArtistsUseCase(id, index)
            relatedArtists.value = result
        }
    }

    fun getArtistAlbums(id:String, limit: Int = 7, index: Int = 0) {
        viewModelScope.launch {
            artistAlbumsLoading = true
            val albums = async { getAlbumsFromArtist(id, limit, index) }.await()
            if (index != 0) {
                val aux = artistAlbums.value.toMutableList()
                aux.addAll(albums)
                artistAlbums.value = aux
            }
            else artistAlbums.value = albums
            artistAlbumsLoading = false
        }

    }

}