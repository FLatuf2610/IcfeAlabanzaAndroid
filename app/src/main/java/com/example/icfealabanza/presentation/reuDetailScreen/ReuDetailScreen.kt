package com.example.icfealabanza.presentation.reuDetailScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.icfealabanza.presentation.global_components.TrackItemSM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReuDetailScreen(reuId: String, viewModel: ReuDetailScreenViewModel, navController: NavController) {

    LaunchedEffect(key1 = reuId) {
        viewModel.getReuById(reuId)
    }

    Scaffold(
        topBar = {
                 TopAppBar(
                     title = { Text(text = viewModel.reu?.name ?: "") },
                     navigationIcon = { IconButton(onClick = { navController.navigateUp() }) {
                         Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                     } },
                     actions = {
                         IconButton(onClick = { /*TODO*/ }) {
                             Icon(imageVector = Icons.Default.Add, contentDescription = "")
                         }
                         IconButton(onClick = { /*TODO*/ }) {
                             Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                         }
                     }
                 )
        },
    ) { pad ->
        if (viewModel.reu == null) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(pad)
            ) {
                items(viewModel.reu!!.tracks) { track ->
                    TrackItemSM(track = track, onClick = {  })
                }
            }
        }

    }
}