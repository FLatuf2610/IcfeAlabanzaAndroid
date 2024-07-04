package com.example.icfealabanza.presentation.artist_detail.popularScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.icfealabanza.presentation.artist_detail.TrackItemIndexed
import com.example.icfealabanza.presentation.global_components.AddBottomSheet
import com.example.icfealabanza.presentation.global_components.TracksBottomSheet
import com.example.icfealabanza.presentation.main.CommonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularTracksScreen(viewModel: PopularTracksViewModel, artistId: String,
                        commonViewModel: CommonViewModel, navController: NavController) {
    LaunchedEffect(key1 = artistId) {
        viewModel.initViewModel(artistId)
    }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Populares") },
                navigationIcon = { IconButton(onClick = { navController.navigateUp() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                } }
            )
        }
    ) { pad ->
        val tracks by viewModel.topTracks.collectAsState()
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(pad),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(tracks) { index, track ->
                TrackItemIndexed(song = track, index = index, onClick = { commonViewModel.showTrackBottomSheet(it) })
                if (index == tracks.lastIndex ) {
                    LaunchedEffect(key1 = true) {
                        viewModel.getTopTracks(artistId, limit = 10, index = index + 1)
                    }
                }
            }
            item {
                if (viewModel.isLoading) CircularProgressIndicator()
            }
        }
        TracksBottomSheet(
            commonViewModel = commonViewModel,
            navController = navController,
            context = context,
            editMode = false
        )
        AddBottomSheet(commonViewModel = commonViewModel)
    }
}