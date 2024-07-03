package com.example.icfealabanza.presentation.global_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.icfealabanza.domain.models.AlbumListItem
import com.example.icfealabanza.domain.models.ArtistListItem
import com.example.icfealabanza.domain.models.Reunion
import com.example.icfealabanza.domain.models.SongListItem
import kotlinx.coroutines.Dispatchers


@Composable
fun TrackItemSM(track: SongListItem, onClick: (SongListItem) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17)),
        onClick = { onClick(track) }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(track.coverSmall)
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
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = track.title,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = track.artist, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
fun AlbumItemSM(album: AlbumListItem, onClick: (AlbumListItem) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17)),
        onClick = { onClick(album) }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(album.coverSmall)
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
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = album.title,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = album.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
fun ArtistItemSM(artist: ArtistListItem, onClick: (ArtistListItem) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17)),
        onClick = { onClick(artist) }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(artist.cover)
                    .dispatcher(Dispatchers.IO)
                    .crossfade(true)
                    .placeholder(null)
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = artist.name,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun ReuItemSM(reu: Reunion, onClick: (Reunion) -> Unit) {
    Surface(
        onClick = { onClick(reu) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRMZ4mXocj9SvaG1-t-DBMZm30QwFzO-MfVSA&s"
                    )
                    .dispatcher(Dispatchers.IO)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(17)),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(text = reu.name)
                Text(text = "${reu.tracks.size} Canciones")
            }
        }
    }
}

@Composable
fun AlbumItemMD(album: AlbumListItem, onClick: (AlbumListItem) -> Unit) {
    Surface(
        onClick = { onClick(album) },
        modifier = Modifier
            .clip(RoundedCornerShape(12)),

        ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(17))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(album.coverSmall)
                        .crossfade(true)
                        .dispatcher(Dispatchers.IO)
                        .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            Column {
                Text(
                    text = album.title,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(160.dp)
                )
                Text(
                    text = album.releaseDate,
                    color = Color.LightGray)
            }
        }
    }
}

@Composable
fun ArtistItemMD(artist: ArtistListItem, onClick: (ArtistListItem) -> Unit) {
    Surface(
        modifier = Modifier.clip(RoundedCornerShape(17)),
        onClick = { onClick(artist) }
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
            ) {
                AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                    .data(artist.cover)
                    .crossfade(true)
                    .dispatcher(Dispatchers.IO)
                    .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier.width(152.dp),
                text = artist.name,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ArtistsList(list: List<ArtistListItem>, title: String, onClick: (ArtistListItem) -> Unit) {
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
        LazyRow {
            items(list) { item ->
                ArtistItemMD(artist = item) { onClick(it) }
            }
        }
    }
}

@Composable
fun BottomSheetContentSong(
    songListItem: SongListItem?,
    webViewLyrics: (SongListItem) -> Unit,
    webViewNotes: (SongListItem) -> Unit,
    editMode: Boolean,
    onDeleteFromReu: (SongListItem) -> Unit
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        BottomSheetRowItem(
            item = songListItem!!,
            onClick = { webViewLyrics(it) },
            action = "Buscar Letra",
            icon = Icons.Default.Search,
        )
        BottomSheetRowItem(
            item = songListItem,
            onClick = { webViewNotes(it) },
            action = "Buscar Notas",
            icon = Icons.Default.Search
        )
        BottomSheetRowItem(
            item = songListItem,
            onClick = {  },
            action = "Agregar a reu",
            icon = Icons.Default.Add)
        if (editMode) {
            BottomSheetRowItem(
                item = songListItem,
                onClick = { onDeleteFromReu(it) },
                action = "Eliminar de la reu",
                icon = Icons.Outlined.Delete
            )
        }
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
fun AddBottomSheetContent(reus: List<Reunion>, onClick: (Reunion) -> Unit, getReus: () -> Unit) {
    LaunchedEffect(key1 = Unit) { getReus() }
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
    ) {
        items(reus) { reu ->
            ReuItemSM(reu = reu, onClick = { onClick(it) })
        }
    }
}