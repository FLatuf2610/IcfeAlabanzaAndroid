package com.example.icfealabanza.presentation.artist_detail

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.icfealabanza.domain.models.AlbumListItem
import com.example.icfealabanza.domain.models.SongListItem
import com.example.icfealabanza.presentation.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetailScreen(
    artistId: String,
    viewModel: ArtistDetailViewModel,
    mainViewModel: MainViewModel,
    navController: NavController,
) {
    LaunchedEffect(key1 = artistId) {
        viewModel.initViewModel(artistId)
    }
    val scope = rememberCoroutineScope()
    val artist by viewModel.artist.collectAsState()
    val albums by viewModel.artistAlbums.collectAsState()
    val relatedArtists by viewModel.relatedArtists.collectAsState()
    val topSongs by viewModel.artistTopSongs.collectAsState()
    val lazyColumnState = rememberLazyListState()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val firstVisibleItem by remember { derivedStateOf { lazyColumnState.firstVisibleItemIndex } }
    val colorTopBar =
        if (firstVisibleItem < 1) Color.Transparent else MaterialTheme.colorScheme.background
    val colorA by animateColorAsState(targetValue = colorTopBar, label = "")
    val colorIcon = if (firstVisibleItem >= 1) Color.Transparent else Color.Black.copy(alpha = 0.2f)
    val colorI by animateColorAsState(targetValue = colorIcon, label = "")
    val title = if (firstVisibleItem < 1) "" else artist?.name ?: ""
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = colorI
                        )
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorA)
            )
        }
    )
    {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = lazyColumnState
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
                ArtistHeader(artist?.name ?: "", artist?.cover ?: "")
                Spacer(modifier = Modifier.height(16.dp))
            }
            artistTopSongs(
                list = if (topSongs.size >= 6) topSongs.subList(0, 5)
                else if (topSongs.isNotEmpty()) topSongs.subList(0, topSongs.size)
                else emptyList(),
                onClick = { mainViewModel.onSongClick(it) },
                onButtonClick = {
                    viewModel.isTopSongsSheetShowed = true
                },
            )
            item {
                ArtistAlbums(
                    list = albums,
                    onClick = { },
                    onFinishScroll = { index ->
                        scope.launch {
                            viewModel.artistAlbumsLoading = true
                            async {
                                viewModel.getArtistAlbums(artistId, index = index)
                                viewModel.artistAlbumsLoading = false
                            }.await()
                        }
                    },
                    isLoading = viewModel.artistAlbumsLoading
                )
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
        if (viewModel.isTopSongsSheetShowed) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.isTopSongsSheetShowed = false },
                sheetState = bottomSheetState
            ) {
                SongsBottomSheetContent(
                    list = topSongs,
                    mainViewModel = mainViewModel,
                    isLoading = viewModel.artistTopSongsLoading,
                    onFinishScroll = { index ->
                        scope.launch {
                            viewModel.artistTopSongsLoading = true
                            async {
                                viewModel.getTopSongs(artistId, 10, index)
                                viewModel.artistAlbumsLoading = false
                            }.await()
                        }
                    }
                )
            }

        }
    }
}

@Composable
fun ArtistHeader(name: String, image: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .crossfade(true)
                .dispatcher(Dispatchers.IO)
                .build(),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )
    }
}

fun LazyListScope.artistTopSongs(
    list: List<SongListItem>,
    onClick: (SongListItem) -> Unit,
    onButtonClick: () -> Unit
) {
    item {
        Text(
            text = "Populares",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
    itemsIndexed(list) { index: Int, item: SongListItem ->
        SongItem(song = item, index = index) { onClick(it) }
    }
    item {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            TextButton(onClick = {
                onButtonClick()
            }) {
                Text(text = "VER MAS")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ArtistAlbums(
    list: List<AlbumListItem>,
    onClick: () -> Unit,
    onFinishScroll: (Int) -> Unit,
    isLoading: Boolean
) {
    Text(
        text = "Albumes",
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        modifier = Modifier.padding(start = 8.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Spacer(modifier = Modifier.width(8.dp)) }
        itemsIndexed(list) { index: Int, item: AlbumListItem ->
            AlbumItem(albumListItem = item) { onClick() }
            if (index == list.lastIndex) {
                onFinishScroll(index + 1)
            }
        }
        item {
            if (isLoading) CircularProgressIndicator()
        }
    }
}

@Composable
fun AlbumItem(albumListItem: AlbumListItem, onClick: () -> Unit) {
    Surface(
        onClick = { onClick() },
        modifier = Modifier.clip(RoundedCornerShape(8.dp))
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(RoundedCornerShape(17))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(albumListItem.coverSmall)
                        .crossfade(true)
                        .dispatcher(Dispatchers.IO)
                        .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            Text(
                text = albumListItem.title,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(200.dp)
            )
            Text(text = albumListItem.releaseDate, color = Color.LightGray)
        }
    }
}

@Composable
fun SongItem(song: SongListItem, index: Int, onClick: (SongListItem) -> Unit) {
    Surface(
        onClick = { onClick(song) },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16))
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = (index + 1).toString(), fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.width(12.dp))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(song.coverSmall)
                    .dispatcher(Dispatchers.IO)
                    .crossfade(true)
                    .placeholder(null)
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(17))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = song.title,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = song.artist)
            }
        }
    }
}

@Composable
fun SongsBottomSheetContent(
    list: List<SongListItem>,
    mainViewModel: MainViewModel,
    onFinishScroll: (Int) -> Unit,
    isLoading: Boolean
) {
    Text(
        text = "Populares",
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        modifier = Modifier.padding(8.dp)
    )
    Spacer(modifier = Modifier.height(4.dp))
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        itemsIndexed(list) { index: Int, item: SongListItem ->
            SongItem(song = item, index = index) { mainViewModel.onSongClick(item) }
            if (list.lastIndex == index) {
                LaunchedEffect(key1 = Unit) {
                    onFinishScroll(index + 1)
                }
            }
        }
        item {
            if (isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}



