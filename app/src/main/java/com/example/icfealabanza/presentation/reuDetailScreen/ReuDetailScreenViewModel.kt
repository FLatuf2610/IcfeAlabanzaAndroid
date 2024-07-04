package com.example.icfealabanza.presentation.reuDetailScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icfealabanza.data.repository.IcfeRepositoryImpl
import com.example.icfealabanza.domain.models.Reunion
import com.example.icfealabanza.domain.models.SongListItem
import com.example.icfealabanza.domain.use_cases.DeleteReuUseCase
import com.example.icfealabanza.domain.use_cases.SearchSongsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReuDetailScreenViewModel @Inject constructor(
    private val repositoryImpl: IcfeRepositoryImpl,
    private val deleteReuUseCase: DeleteReuUseCase,
    private val searchSongsUseCase: SearchSongsUseCase,
): ViewModel() {

    var reu by mutableStateOf<Reunion?>(null)
        private set
    private val _searchedTracks = MutableStateFlow<List<SongListItem>>(emptyList())
    val searchedTracks = _searchedTracks.asStateFlow()
    var isDeleteDialogShowed by mutableStateOf(false)
        private set
    fun showDeleteDialog() {
        isDeleteDialogShowed = true
    }
    fun hideDeleteDialog() {
        isDeleteDialogShowed = false
    }

    var isAddToReuBottomSheetShowed by mutableStateOf(false)
        private set
    fun showAddToReuBottomSheet() {
        isAddToReuBottomSheetShowed = true
    }
    fun hideAddToReuBottomSheet() {
        isAddToReuBottomSheetShowed = false
        _searchedTracks.value = emptyList()
    }

    private var searchJob: Job? = null
    fun search(query: String) {
        searchJob?.cancel()
        if (query.isEmpty()) {
            _searchedTracks.value = emptyList()
            return
        }
        searchJob = viewModelScope.launch {
            delay(300)
            val songs = async { searchSongsUseCase(query, limit = 15) }.await()
            _searchedTracks.value = songs
            Log.i("SONGS", songs.toString())
        }
    }

    fun getReuById(reuId: String) {
        repositoryImpl.getReuById(
            id = reuId,
            onSuccess = { reu = it },
            onError = { Log.e("ERROR FIREBASE", it.message!!) }
        )
    }

    fun deleteReu(reuId: String) {
        viewModelScope.launch {
            deleteReuUseCase(
                reuId = reuId,
                onSuccess = {},
                onError = {}
            )
        }

    }
}