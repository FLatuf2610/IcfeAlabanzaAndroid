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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.icfealabanza.presentation.global_components.AlbumItemMD
import com.example.icfealabanza.presentation.global_components.ArtistsList
import kotlinx.coroutines.Dispatchers

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetailScreen(
    artistId: String,
    viewModel: ArtistDetailViewModel,
    navController: NavController,
) {

    LaunchedEffect(key1 = true) {
        viewModel.initViewModel(artistId)
    }


    val artist by viewModel.artist.collectAsState()
    val albums by viewModel.artistAlbums.collectAsState()
    val relatedArtists by viewModel.relatedArtists.collectAsState()
    val topSongs by viewModel.artistTopSongs.collectAsState()
    val lazyColumnState = rememberLazyListState()
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
                        onClick = { navController.navigateUp() },
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

        if (viewModel.isLoadingState) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
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
                    list = topSongs.take(5),
                    onClick = {  },
                    onButtonClick = { /*TODO: Navigate to Popular Screen*/ },
                )
                artistAlbums(
                    list = albums,
                    onClick = { item -> navController.navigate("album_detail/${item.id}") },
                    onFinishScroll = { index -> viewModel.getArtistAlbums(artistId, 7, index) },
                    isLoading = viewModel.artistAlbumsLoading
                )

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    ArtistsList(
                        list = relatedArtists,
                        title = "Artistas Similares",
                        onClick = { navController.navigate("artist_detail/${it.id}") }
                    )

                }
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
                .size(180.dp)
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
    itemsIndexed(
        list,
        key = { _, item -> item.id }
    ) { index: Int, item: SongListItem ->
        TrackItemIndexed(song = item, index = index) { onClick(it) }
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

fun LazyListScope.artistAlbums(
    list: List<AlbumListItem>,
    onClick: (AlbumListItem) -> Unit,
    onFinishScroll: (Int) -> Unit,
    isLoading: Boolean
) {
    item {
        Text(
            text = "Albumes",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
    item {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            itemsIndexed(
                list,
                key = { _, item ->  item.id}
            ) { index: Int, item: AlbumListItem ->
                AlbumItemMD(album = item) { onClick(item) }
                if (index == list.lastIndex) {
                    LaunchedEffect(key1 = Unit) {
                        onFinishScroll(index + 1)
                    }

                }
            }
            item {
                if (isLoading) CircularProgressIndicator()
            }
        }
    }
}



@Composable
fun TrackItemIndexed(song: SongListItem, index: Int, onClick: (SongListItem) -> Unit) {
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




