package com.example.icfealabanza.presentation.artist_detail.popularScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icfealabanza.domain.models.SongListItem
import com.example.icfealabanza.domain.use_cases.GetArtistTopSongs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularTracksViewModel @Inject constructor(
    private val getArtistTopSongs: GetArtistTopSongs
) : ViewModel() {

    private var _topTracks = MutableStateFlow<List<SongListItem>>(emptyList())
    val topTracks = _topTracks.asStateFlow()
    var isLoading by mutableStateOf(false)
        private set
    private var artistId = ""

    fun initViewModel(artistId: String) {
        if (artistId == this.artistId) {
            return
        }
        this.artistId = artistId
        getTopTracks(artistId)
    }
    fun getTopTracks(artistId: String, limit: Int = 15 ,index: Int = 0) {
        isLoading = true
        viewModelScope.launch {
            val tracks = async { getArtistTopSongs(id = artistId, limit = limit, index) }.await()
            if (index == 0) {
                _topTracks.value = tracks
            } else {
                val aux = _topTracks.value.toMutableList()
                aux.addAll(tracks)
                _topTracks.value = aux.toList()
            }
            isLoading = false
        }
    }
}