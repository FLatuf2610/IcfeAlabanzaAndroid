package com.example.icfealabanza.presentation.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.icfealabanza.domain.models.Reunion
import com.example.icfealabanza.domain.models.SongListItem
import com.example.icfealabanza.domain.use_cases.AddToReuUseCase
import com.example.icfealabanza.domain.use_cases.DeleteTrackFromReuUseCase
import com.example.icfealabanza.domain.use_cases.GetSingleReusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(
    private val addToReuUseCase: AddToReuUseCase,
    private val getSingleReusUseCase: GetSingleReusUseCase,
    private val deleteTrackFromReuUseCase: DeleteTrackFromReuUseCase
): ViewModel() {

    var selectedTrack by mutableStateOf<SongListItem?>(null)
        private set
    var isTrackBottomSheetShowed by mutableStateOf(false)
        private set
    fun showTrackBottomSheet(track: SongListItem) {
        selectedTrack = track
        isTrackBottomSheetShowed = true
    }
    fun hideTrackBottomSheet() {
        selectedTrack = null
        isTrackBottomSheetShowed = false
    }

    var reus by mutableStateOf<List<Reunion>?>(null)
        private set
    var snackBarText by mutableStateOf<String?>(null)
        private set
    fun resetSnackBarText() {
        snackBarText = null
    }
    var isAddBottomSheetShowed by mutableStateOf(false)
        private set
    var addBottomSheetTrack by mutableStateOf<SongListItem?>(null)
    fun showAddBottomSheet(track: SongListItem) {
        addBottomSheetTrack = track
        isAddBottomSheetShowed = true
    }
    fun hideAddBottomSheet() {
        addBottomSheetTrack = null
        isAddBottomSheetShowed = false
    }
    fun addToReu(reuId: String, track: SongListItem) {
        addToReuUseCase(
            idReu = reuId,
            track = track,
            onErrorS = { snackBarText = it },
            onSuccess = { snackBarText = it },
            onError = { snackBarText = it.message })
    }
    fun getSingleReus() {
        getSingleReusUseCase(
            onSuccess = {reus = it},
            onError = { Log.e("FIREBASE ERROR", it.message!!)}
        )
    }
    fun deleteTrack(reuId: String, track: SongListItem) {
        deleteTrackFromReuUseCase(
            reuId = reuId,
            track = track,
            onSuccess = { snackBarText = it },
            onErrorS = { snackBarText = it },
            onError = { snackBarText = it.message }
        )
    }

    fun navigateToLyrics(track: SongListItem, navController: NavController) {
        val titleSplit = track.title.replace(" ", "+")
        val artistSplit = track.artist.replace(" ", "+")
        val query = "$titleSplit+$artistSplit+lyrics"
        navController.navigate("web_view/${query}")
    }

    fun navigateToNotes(track: SongListItem, context: Context) {
        val titleSplit = track.title.replace(" ", "+")
        val artistSplit = track.artist.replace(" ", "+")
        val url = "https://www.google.com/search?q=$titleSplit+$artistSplit+notas+la+cuerda"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        context.startActivity(intent)
    }
}