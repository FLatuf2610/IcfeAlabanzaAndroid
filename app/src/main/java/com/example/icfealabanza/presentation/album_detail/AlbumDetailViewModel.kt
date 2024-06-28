package com.example.icfealabanza.presentation.album_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icfealabanza.domain.models.AlbumDetail
import com.example.icfealabanza.domain.models.SongListItem
import com.example.icfealabanza.domain.use_cases.GetAlbumByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(private val getAlbumByIdUseCase: GetAlbumByIdUseCase): ViewModel() {
    var album = MutableStateFlow<AlbumDetail?>(null)
        private set
    fun initViewModel(albumId: String) {
        viewModelScope.launch {
            async {
                val result = getAlbumByIdUseCase(albumId)
                if (result != null) {
                    album.value = result
                }
            }.await()
        }
    }
}