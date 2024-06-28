package com.example.icfealabanza.presentation.album_detail

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.icfealabanza.presentation.main.MainViewModel
import com.example.icfealabanza.presentation.search.SearchListItem
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AlbumDetailScreen(
    albumId: String,
    mainViewModel: MainViewModel,
    viewModel: AlbumDetailViewModel,
    navController: NavController
) {
    LaunchedEffect(key1 = albumId) {
        viewModel.initViewModel(albumId)
    }
    val album by viewModel.album.collectAsState()
    val lazyColumnState = rememberLazyListState()
    val firstVisibleItem by remember { derivedStateOf { lazyColumnState.firstVisibleItemIndex } }
    val colorTopBar =
        if (firstVisibleItem < 1) Color.Transparent else MaterialTheme.colorScheme.background
    val colorA by animateColorAsState(targetValue = colorTopBar, label = "")
    val colorIcon = if (firstVisibleItem >= 1) Color.Transparent else Color.Black.copy(alpha = 0.2f)
    val colorI by animateColorAsState(targetValue = colorIcon, label = "")
    val title = if (firstVisibleItem < 1) "" else album?.title ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() } ,
                        colors = IconButtonDefaults.iconButtonColors(containerColor = colorI)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorA
                )
            )
        }
    ) { _ ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyColumnState
        ) {
            item {
                AlbumHeader(name = album?.title ?: "", image = album?.cover ?:"")
            }
            items(album?.tracks ?: emptyList()) { item ->
                SearchListItem(id = item.id, title = item.title, imageUrl = item.coverSmall, subTitle = item.artist) {
                    mainViewModel.onSongClick(item)
                }
            }
        }
    }
}



@Composable
fun AlbumHeader(name: String, image: String) {
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
                .clip(RoundedCornerShape(17))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )
    }
}