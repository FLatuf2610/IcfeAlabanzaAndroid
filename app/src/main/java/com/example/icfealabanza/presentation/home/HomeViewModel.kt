package com.example.icfealabanza.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icfealabanza.common.constants.HILLSONG_ESP_ID
import com.example.icfealabanza.domain.models.ArtistListItem
import com.example.icfealabanza.domain.use_cases.GetRelatedArtistsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRelatedArtistsUseCase: GetRelatedArtistsUseCase
): ViewModel() {

    var relatedArtistsList = MutableStateFlow<List<ArtistListItem>>(emptyList())
        private set

    init {
        initViewModel()
    }

    private fun initViewModel() {
        getRelatedArtists()
    }

    private fun getRelatedArtists() {
        viewModelScope.launch {
            relatedArtistsList.value = getRelatedArtistsUseCase(HILLSONG_ESP_ID)
        }
    }

}