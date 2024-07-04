package com.example.icfealabanza.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icfealabanza.domain.use_cases.SearchAlbumUseCase
import com.example.icfealabanza.domain.use_cases.SearchArtistsUseCase
import com.example.icfealabanza.domain.use_cases.SearchSongsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchSongsUseCase: SearchSongsUseCase,
    private val searchArtistsUseCase: SearchArtistsUseCase,
    private val searchAlbumUseCase: SearchAlbumUseCase,
) : ViewModel() {

    var state = MutableStateFlow(SearchState())
        private set
    var isLoadingSongs by mutableStateOf(false)
    var isLoadingAlbums by mutableStateOf(false)
    var isLoadingArtists by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    private var searchJob: Job? = null
    var query by mutableStateOf("")
    fun search(query: String) {
        isLoading = false
        searchJob?.cancel()
        if (query.isEmpty()) {
            state.value = SearchState()
            return
        }
        searchJob = viewModelScope.launch {
            isLoading = true
            delay(300)
            val songs = async { searchSongsUseCase(query) }.await()
            val artists = async { searchArtistsUseCase(query) }.await()
            val album = async { searchAlbumUseCase(query) }.await()
            val newState = state.value.copy(
                albums = album.toMutableList(),
                artists = artists.toMutableList(),
                songs = songs.toMutableList()
            )
            state.value = newState
            isLoading = false
        }
    }

    fun loadMoreSongs(query: String, index: Int) {
        viewModelScope.launch {
            isLoadingSongs = true
            val songsR = async { searchSongsUseCase(query = query, index = index) }.await()
            val aux = state.value.songs
            aux.addAll(songsR)
            val newState = state.value.copy(songs = aux)
            state.value = newState
            isLoadingSongs = false
        }
    }

    fun loadMoreAlbums(query: String, index: Int) {
        viewModelScope.launch {
            isLoadingAlbums = true
            val albumsR = async { searchAlbumUseCase(query = query, index = index) }.await()
            val aux = state.value.albums
            aux.addAll(albumsR)
            val newState = state.value.copy(albums = aux)
            state.value = newState
            isLoadingAlbums = false
        }
    }

    fun loadMoreArtists(query: String, index: Int) {
        viewModelScope.launch {
            isLoadingArtists = true
            val artistsR = async { searchArtistsUseCase(query = query, index = index) }.await()
            val aux = state.value.artists
            aux.addAll(artistsR)
            val newState = state.value.copy(artists = aux)
            state.value = newState
            isLoadingArtists = false
        }
    }

}