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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel, navController: NavHostController) {
    val state by viewModel.state.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }
    val lazyListState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(key1 = lazyListState.isScrollInProgress) {
       if (lazyListState.isScrollInProgress) keyboardController?.hide()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            query = query,
            onQueryChange = {
                query = it
                viewModel.search(it)
            },
            onSearch = {},
            active = true,
            onActiveChange = {},
            leadingIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
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
                            onButtonClick = { idx -> viewModel.loadMoreSongs(query, idx) },
                            onClick = { viewModel.onSongClick(it) })
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
                            onButtonClick = { idx -> viewModel.loadMoreArtists(query, idx) },
                            onClick = { }
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
                            onButtonClick = { idx -> viewModel.loadMoreAlbums(query, idx) },
                            onClick = { navController }
                        )
                    }


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
        if (viewModel.isBottomSheetVisible) {
            ModalBottomSheet(onDismissRequest = { viewModel.hideBottomSheet() }) {
                BottomSheetContent(songListItem = viewModel.selectedItem, webViewLyrics = {}, webViewNotes = {}) { }

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
    items(list) { song ->
        SearchListItem(
            id = song.id,
            title = song.title,
            subTitle = song.artist,
            imageUrl = song.coverSmall
        ) {
            onClick(song)
        }
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
        SearchListItem(
            id = artist.id,
            title = artist.name,
            imageUrl = artist.cover,
            subTitle = ""
        ) { id ->
            onClick(id)
        }
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
        SearchListItem(
            id = album.id,
            title = album.title,
            imageUrl = album.coverSmall,
            subTitle = album.name
        ) { id ->
            onClick(id)
        }
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

@Composable
fun SearchListItem(
    id: String,
    title: String,
    imageUrl: String?,
    subTitle: String,
    onClick: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        onClick = { onClick(id) }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .dispatcher(Dispatchers.IO)
                    .crossfade(true)
                    .placeholder(null)
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (subTitle.isNotBlank())
                    Text(text = subTitle, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
fun BottomSheetContent(
    songListItem: SongListItem?,
    webViewLyrics: (SongListItem) -> Unit,
    webViewNotes: (SongListItem) -> Unit,
    detail: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(songListItem?.coverSmall ?: "")
                    .dispatcher(Dispatchers.IO)
                    .crossfade(true)
                    .placeholder(null)
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = songListItem?.title ?: "",
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (songListItem?.artist?.isNotBlank()!!)
                    Text(text = songListItem.artist, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
        Surface(
            onClick = { detail(songListItem!!.id) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row {
                Icon(imageVector = Icons.Default.Info, contentDescription = "")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Ir a detalle")
            }
        }

        Surface(
            onClick = { webViewLyrics(songListItem!!) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row {
                Icon(imageVector = Icons.Default.Search, contentDescription = "")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Buscar letra")
            }
        }

        Surface(
            onClick = { webViewNotes(songListItem!!) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row {
                Icon(imageVector = Icons.Default.Search, contentDescription = "")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Buscar Notas")
            }
        }

    }
}
