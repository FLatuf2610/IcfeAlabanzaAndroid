package com.example.icfealabanza.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.icfealabanza.domain.models.AlbumListItem
import com.example.icfealabanza.domain.models.ArtistListItem
import com.example.icfealabanza.domain.models.SongListItem
import com.example.icfealabanza.presentation.global_components.AlbumItemSM
import com.example.icfealabanza.presentation.global_components.ArtistItemSM
import com.example.icfealabanza.presentation.global_components.TrackItemSM
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel, navController: NavHostController) {
    val state by viewModel.state.collectAsState()
    val lazyListState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(key1 = lazyListState.isScrollInProgress) {
        if (lazyListState.isScrollInProgress && keyboardController != null) keyboardController.hide()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            query = viewModel.query,
            onQueryChange = {
                viewModel.query = it
                viewModel.search(it)
            },
            onSearch = {},
            active = true,
            onActiveChange = {},
            leadingIcon = {
                IconButton(onClick = {
                    keyboardController?.hide()
                    navController.navigateUp()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            placeholder = { Text(text = "Buscar...") }
        ) {
            if (state.albums.isNotEmpty() || state.artists.isNotEmpty() || state.songs.isNotEmpty()) {
                LazyColumn(
                    state = lazyListState
                ) {
                    if (state.songs.isNotEmpty()) {
                        item {
                            Text(
                                text = "CANCIONES",
                                color = Color.LightGray,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        songList(
                            list = state.songs,
                            isLoading = viewModel.isLoadingSongs,
                            onButtonClick = { idx -> viewModel.loadMoreSongs(viewModel.query, idx) },
                            onClick = { keyboardController?.hide() })
                    }
                    if (state.artists.isNotEmpty()) {
                        item {
                            Text(
                                text = "ARTISTAS",
                                color = Color.LightGray,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        artistsList(
                            list = state.artists,
                            isLoading = viewModel.isLoadingArtists,
                            onButtonClick = { idx -> viewModel.loadMoreArtists(viewModel.query, idx) },
                            onClick = {
                                keyboardController?.hide()
                                navController.navigate("artist_detail/$it")
                            }
                        )
                    }
                    if (state.albums.isNotEmpty()) {
                        item {
                            Text(
                                text = "ALBUMS",
                                color = Color.LightGray,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        albumsList(
                            list = state.albums,
                            isLoading = viewModel.isLoadingAlbums,
                            onButtonClick = { idx -> viewModel.loadMoreAlbums(viewModel.query, idx) },
                            onClick = {  }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(92.dp)) }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Busca canciones, artistas o albumes",
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }


    }
}


fun LazyListScope.songList(
    list: List<SongListItem>,
    isLoading: Boolean,
    onButtonClick: (Int) -> Unit,
    onClick: (SongListItem) -> Unit
) {
    items(list) { track ->
        TrackItemSM(track = track, onClick = { onClick(it) })
    }
    item {
        if (isLoading) Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        else TextButton(onClick = { onButtonClick(list.size) }) {
            Text(text = "Cargar mas")
        }
    }
}

fun LazyListScope.artistsList(
    list: List<ArtistListItem>,
    isLoading: Boolean,
    onButtonClick: (Int) -> Unit,
    onClick: (String) -> Unit
) {
    items(list) { artist ->
        ArtistItemSM(artist = artist, onClick = { onClick(it.id) })
    }
    item {
        if (isLoading) Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        else TextButton(onClick = { onButtonClick(list.size) }) {
            Text(text = "Cargar mas")
        }
    }
}

fun LazyListScope.albumsList(
    list: List<AlbumListItem>,
    isLoading: Boolean,
    onButtonClick: (Int) -> Unit,
    onClick: (String) -> Unit
) {
    items(list) { album ->
        AlbumItemSM(album = album, onClick = { onClick(it.id) })
    }
    item {
        if (isLoading) Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        else TextButton(onClick = { onButtonClick(list.size) }) {
            Text(text = "Cargar mas")
        }
    }
}




