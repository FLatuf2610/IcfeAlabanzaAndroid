package com.example.icfealabanza.presentation.main

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.icfealabanza.common.constants.LYRICS_MODE
import com.example.icfealabanza.common.constants.NOTES_MODE
import com.example.icfealabanza.domain.models.SongListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val mediaPlayer = MediaPlayer()
    var currentPlayingSong by mutableStateOf<SongListItem?>(null)
        private set
    var isPlaying by mutableStateOf(false)

    var selectedItem by mutableStateOf<SongListItem?>(null)
        private set
    var isBottomSheetVisible by mutableStateOf(false)
        private set

    init {
        initMediaPlayer()
        mediaPlayer.setOnCompletionListener {
            isPlaying = false
        }
    }

    private fun initMediaPlayer() {
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
    }

    fun onSongClick(song: SongListItem) {
        selectedItem = song
        isBottomSheetVisible = true
    }

    fun hideBottomSheet() {
        isBottomSheetVisible = false
        selectedItem = null
    }

    fun playSong(songListItem: SongListItem) {
        if (currentPlayingSong == songListItem) {
            isPlaying = true
            Log.e("CANCION ACTUAL", currentPlayingSong?.preview ?: "No hay")
            Log.e("CANCION ES LA MISMA", (songListItem == currentPlayingSong).toString())
            mediaPlayer.start()
            return
        }
        currentPlayingSong = songListItem
        Log.e("CANCION ACTUAL", currentPlayingSong?.preview ?: "No hay")
        isPlaying = true
        mediaPlayer.reset()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mediaPlayer.setDataSource(songListItem.preview)
                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (e: Exception) {
                Log.e("ERROR REPRODUCIENDO", e.message!!)
                withContext(Dispatchers.Main) {
                    isPlaying = false
                }
            }
        }
    }

    fun stopSong() {
        if (isPlaying) mediaPlayer.pause()
        isPlaying = false
    }

    fun navigateToLyrics(song: SongListItem, navController: NavController) {
        val title = song.title.replace(" ", "+")
        val artist = song.artist.replace(" ", "+")
        val query = "$title+$artist"
        val mode = LYRICS_MODE
        navController.navigate("web_view/$query/$mode")
    }

    fun navigateToNotes(song: SongListItem, navController: NavController) {
        val title = song.title.replace(" ", "+")
        val artist = song.artist.replace(" ", "+")
        val query = "$title+$artist"
        val mode = NOTES_MODE
        navController.navigate("web_view/$query/$mode")
    }

}