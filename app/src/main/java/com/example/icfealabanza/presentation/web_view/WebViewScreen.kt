package com.example.icfealabanza.presentation.web_view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(navController: NavController, query: String) {
    val context = LocalContext.current
    val url = "https://www.google.com/search?q=$query+letra"
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                }
            )
        }
    ) { pad->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(pad),
        ) {
            WebViewComponent(context = context, url = url)
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewComponent(context: Context, url: String) {

    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.loadWithOverviewMode = true
            webViewClient = WebViewClient()
            Log.i("URL", url)
            loadUrl(url)
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { webView })
    }
}