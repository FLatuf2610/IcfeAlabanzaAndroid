package com.example.icfealabanza.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.icfealabanza.domain.models.SongListItem
import com.example.icfealabanza.presentation.album_detail.AlbumDetailScreen
import com.example.icfealabanza.presentation.album_detail.AlbumDetailViewModel
import com.example.icfealabanza.presentation.artist_detail.ArtistDetailScreen
import com.example.icfealabanza.presentation.artist_detail.ArtistDetailViewModel
import com.example.icfealabanza.presentation.home.HomeScreen
import com.example.icfealabanza.presentation.home.HomeViewModel
import com.example.icfealabanza.presentation.search.SearchScreen
import com.example.icfealabanza.presentation.search.SearchViewModel
import com.example.icfealabanza.presentation.web_view.WebViewScreen
import com.example.icfealabanza.ui.theme.IcfeAlabanzaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val mainViewModel: MainViewModel = hiltViewModel()
            val searchViewModel: SearchViewModel = hiltViewModel()
            val homeViewModel: HomeViewModel = hiltViewModel()
            val artistDetailViewModel: ArtistDetailViewModel = hiltViewModel()
            val albumDetailViewModel: AlbumDetailViewModel = hiltViewModel()

            IcfeAlabanzaTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        NavHost(navController = navController, startDestination = "home") {
                            composable(
                                "home",
                                popEnterTransition = { fadeIn() })
                                {
                                HomeScreen(
                                    mainViewModel = mainViewModel,
                                    viewModel = homeViewModel,
                                    navController = navController
                                )
                            }
                            composable(
                                "search",
                                enterTransition = {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(200))
                                },
                                exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(200)) }
                                ) {
                                SearchScreen(
                                    viewModel = searchViewModel,
                                    mainViewModel = mainViewModel,
                                    navController = navController
                                )
                            }
                            composable(
                                "album_detail/{id}",
                                arguments = listOf(navArgument("id") { type = NavType.StringType }),
                                enterTransition = {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(200))
                                },
                                exitTransition = {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)
                                },
                                popEnterTransition = { fadeIn() }
                            ) {
                                val id = it.arguments?.getString("id")!!
                                AlbumDetailScreen(
                                    albumId = id,
                                    mainViewModel = mainViewModel,
                                    viewModel = albumDetailViewModel,
                                    navController = navController
                                )
                            }
                            composable(
                                "artist_detail/{id}",
                                arguments = listOf(navArgument("id") { type = NavType.StringType }),
                                enterTransition = {
                                   slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(200))
                                },
                                exitTransition = {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)
                                },
                                popEnterTransition = { fadeIn() }
                            ) {
                                val id = it.arguments?.getString("id")!!
                                ArtistDetailScreen(
                                    artistId = id,
                                    viewModel = artistDetailViewModel,
                                    navController = navController,
                                    mainViewModel = mainViewModel
                                )
                            }
                            composable(
                                "web_view/{query}",
                                arguments = listOf(
                                    navArgument("query") { type = NavType.StringType }
                                ),
                                enterTransition = {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(200))
                                },
                                exitTransition = {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)
                                },
                            ) {
                                val query = it.arguments?.getString("query")!!
                                WebViewScreen(
                                    navController = navController,
                                    query = query,
                                )
                            }
                        }


                        if (mainViewModel.currentPlayingSong != null)
                            BottomPlayingSong(
                                item = mainViewModel.currentPlayingSong!!,
                                isPlayingF = { it == mainViewModel.currentPlayingSong && mainViewModel.isPlaying },
                                onClick = { mainViewModel.playSong(it) },
                                onStopPreview = { mainViewModel.stopSong() },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .zIndex(1000f)
                            )
                        if (mainViewModel.isBottomSheetVisible) {
                            ModalBottomSheet(onDismissRequest = { mainViewModel.hideBottomSheet() }) {
                                val context = LocalContext.current
                                BottomSheetContentSong(
                                    songListItem = mainViewModel.selectedItem,
                                    webViewLyrics = {
                                        mainViewModel.hideBottomSheet()
                                        mainViewModel.navigateToLyrics(it, navController)
                                    },
                                    webViewNotes = {
                                        mainViewModel.hideBottomSheet()
                                        mainViewModel.navigateToNotes(it, context)
                                    },
                                    isPlayingPreview = mainViewModel.currentPlayingSong == mainViewModel.selectedItem && mainViewModel.isPlaying,
                                    playSong = { mainViewModel.playSong(it) },
                                    onStopPreview = { mainViewModel.stopSong() },
                                    viewModel = mainViewModel
                                )

                            }
                        }
                    }
                }
            }

        }
    }

}

@Composable
fun BottomPlayingSong(
    item: SongListItem,
    isPlayingF: (SongListItem) -> Boolean,
    onClick: (SongListItem) -> Unit,
    onStopPreview: () -> Unit,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(17))
            .background(Color.Black)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(27))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.coverSmall)
                        .dispatcher(Dispatchers.IO)
                        .crossfade(true)
                        .placeholder(null)
                        .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = item.artist, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            IconButton(onClick = { if (isPlayingF(item)) onStopPreview() else onClick(item) }) {
                val icon = if (isPlayingF(item)) Icons.Default.Close else Icons.Default.PlayArrow
                Icon(imageVector = icon, contentDescription = null)
            }
        }
    }

}

@Composable
fun BottomSheetContentSong(
    songListItem: SongListItem?,
    webViewLyrics: (SongListItem) -> Unit,
    webViewNotes: (SongListItem) -> Unit,
    playSong: (SongListItem) -> Unit,
    onStopPreview: () -> Unit,
    viewModel: MainViewModel,
    isPlayingPreview: Boolean,

    ) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        BottomSheetRowItemPlaySong(
            songListItem = songListItem!!,
            isPlaying = { viewModel.currentPlayingSong == it && viewModel.isPlaying },
            onClick = { playSong(it) },
            icon = if (isPlayingPreview) Icons.Rounded.Clear else Icons.Default.PlayArrow,
            onStopPreview = { onStopPreview() }
        )
        BottomSheetRowItem(
            item = songListItem,
            onClick = { webViewLyrics(songListItem) },
            action = "Buscar Letra",
            icon = Icons.Default.Search,
        )
        BottomSheetRowItem(
            item = songListItem,
            onClick = { webViewNotes(songListItem) },
            action = "Buscar Notas",
            icon = Icons.Default.Search
        )
    }
}

@Composable
fun BottomSheetRowItem(
    item: SongListItem,
    onClick: (SongListItem) -> Unit,
    action: String,
    icon: ImageVector
) {
    Surface(
        onClick = { onClick(item) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = "")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = action)
        }
    }
}

@Composable
fun BottomSheetRowItemPlaySong(
    songListItem: SongListItem,
    isPlaying: (SongListItem) -> Boolean,
    onClick: (SongListItem) -> Unit,
    onStopPreview: () -> Unit,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .padding(0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(songListItem.coverSmall)
                .dispatcher(Dispatchers.IO)
                .crossfade(true)
                .placeholder(null)
                .build(),
            contentDescription = "",
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(17))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = songListItem.title,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = songListItem.artist, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        IconButton(onClick = {
            if (isPlaying(songListItem)) onStopPreview() else onClick(
                songListItem
            )
        }) {
            Icon(imageVector = icon, contentDescription = "")
        }
    }
}
