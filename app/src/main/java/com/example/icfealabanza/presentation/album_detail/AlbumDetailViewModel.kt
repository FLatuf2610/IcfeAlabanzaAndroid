package com.example.icfealabanza.presentation.album_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icfealabanza.domain.models.AlbumDetail
import com.example.icfealabanza.domain.use_cases.GetAlbumByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(private val getAlbumByIdUseCase: GetAlbumByIdUseCase): ViewModel() {

    var albumId = ""
    var album = MutableStateFlow<AlbumDetail?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set

    fun initViewModel(albumId: String) {
        if (albumId == this.albumId) return
        this.albumId = albumId
        viewModelScope.launch {
            isLoading = true
            async {
                val result = getAlbumByIdUseCase(albumId)
                if (result != null) {
                    album.value = result
                }
            }.await()
            isLoading = false
        }
    }
}