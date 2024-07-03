package com.example.icfealabanza.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.icfealabanza.R
import com.example.icfealabanza.presentation.global_components.ArtistsList

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    val relatedArtistsHillsong by viewModel.relatedArtistsList.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ICFE Alabanza") },
                actions = {
                    IconButton(onClick = { navController.navigate("search") }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "")
                    }
                }
            )
        }
    ) { pad ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(pad)
        ) {
            item {
                VersiculoLema()
            }
            item {
                ArtistsList(
                    list = relatedArtistsHillsong,
                    "Similares a Hillsong",
                    onClick = { navController.navigate("artist_detail/${it.id}") },
                )
            }
        }

    }
}

@Composable
fun VersiculoLema() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondo_versiculos1),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column {
                    Text(
                        text = "Versiculo Lema",
                        color = Color.White
                    )
                    Text(
                        text = "Mateo 28:19-20",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
                Text(
                    text = "9 Por tanto, id y haced discípulos a todas las naciones, bautizándolos en el nombre del Padre, y del Hijo, y del Espíritu Santo; 20 enseñándoles que guarden todas las cosas que os he mandado; y he aquí, yo estoy con vosotros todos los días, hasta el fin del mundo. Amén.",
                    color = Color.White,
                )


            }
        }
    }
}

