package com.example.icfealabanza.presentation.reusScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.icfealabanza.presentation.global_components.ReuItemSM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReusScreen(
    viewModel: ReusViewModel,
    navController: NavController
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getReus()
    }

    val reus by viewModel.reus.collectAsState()
    var reuName by rememberSaveable { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reuniones") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                }
            )
        },
        floatingActionButton = { ExtendedFloatingActionButton(
            onClick = { viewModel.toggleModal() },
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "")
            Text(text = "Agregar Reu")
        } },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { pad ->

        if (reus == null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        else if (reus!!.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Todavia no hay reuniones programadas!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(pad)
                    .fillMaxSize()
            ) {
                items(reus!!) {reu ->
                    ReuItemSM(reu = reu, onClick = { navController.navigate("reu_detail/${reu.id}") })
                }
            }
        }
    }

    if (viewModel.addModalOpen) {
        AlertDialog(
            onDismissRequest = { viewModel.toggleModal() },
        ) {
            AddDialogContent(
                reuName = reuName,
                onChange = { reuName = it },
                onSubmit = { viewModel.saveReu(reuName, snackbarHostState) },
                isLoading = viewModel.isLoading)
        }
    }
}

@Composable
fun AddDialogContent(reuName: String, onChange: (String) -> Unit, onSubmit: () -> Unit, isLoading: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17))
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Agregar Reunion",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = reuName,
                onValueChange = { onChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                singleLine = true,
                maxLines = 1,
                label = { Text(text = "Nombre") },
                shape = RoundedCornerShape(17)
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = { onSubmit() },
                    enabled = !isLoading && reuName.isNotBlank(),
                ) {
                    Text(text = "Agregar Reu")
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}