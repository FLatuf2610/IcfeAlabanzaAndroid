package com.example.icfealabanza.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.icfealabanza.presentation.album_detail.AlbumDetailScreen
import com.example.icfealabanza.presentation.album_detail.AlbumDetailViewModel
import com.example.icfealabanza.presentation.artist_detail.ArtistDetailScreen
import com.example.icfealabanza.presentation.artist_detail.ArtistDetailViewModel
import com.example.icfealabanza.presentation.home.HomeScreen
import com.example.icfealabanza.presentation.home.HomeViewModel
import com.example.icfealabanza.presentation.reuDetailScreen.ReuDetailScreen
import com.example.icfealabanza.presentation.reuDetailScreen.ReuDetailScreenViewModel
import com.example.icfealabanza.presentation.reusScreen.ReusScreen
import com.example.icfealabanza.presentation.reusScreen.ReusViewModel
import com.example.icfealabanza.presentation.search.SearchScreen
import com.example.icfealabanza.presentation.search.SearchViewModel
import com.example.icfealabanza.presentation.web_view.WebViewScreen
import com.example.icfealabanza.ui.theme.IcfeAlabanzaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val commonViewModel: CommonViewModel = hiltViewModel()
            val searchViewModel: SearchViewModel = hiltViewModel()
            val homeViewModel: HomeViewModel = hiltViewModel()
            val reuDetailScreenViewModel: ReuDetailScreenViewModel = hiltViewModel()
            val artistDetailViewModel: ArtistDetailViewModel = hiltViewModel()
            val albumDetailViewModel: AlbumDetailViewModel = hiltViewModel()
            val reusViewModel: ReusViewModel = hiltViewModel()
            val snackbarHostState = remember { SnackbarHostState() }

            IcfeAlabanzaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = { BottomNav(navController = navController) },
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState )
                        }
                    ) { pad ->
                        Box(
                            modifier = Modifier
                                .padding(pad)
                                .fillMaxSize()

                        ) {
                            NavHost(navController = navController, startDestination = "home") {
                                composable(
                                    "home",
                                    popEnterTransition = { fadeIn() })
                                {
                                    HomeScreen(viewModel = homeViewModel, navController = navController)
                                }
                                composable("reus") {
                                    ReusScreen(viewModel = reusViewModel, navController = navController)
                                }
                                composable("search") {
                                    SearchScreen(viewModel = searchViewModel,
                                        navController = navController,
                                        commonViewModel = commonViewModel)
                                }
                                composable(
                                    "album_detail/{id}",
                                    arguments = listOf(navArgument("id") { type = NavType.StringType }),
                                    enterTransition = {
                                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(400))
                                    },
                                    exitTransition = {
                                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)
                                    },
                                    popEnterTransition = { fadeIn() }
                                ) {
                                    val id = it.arguments?.getString("id")!!
                                    AlbumDetailScreen(
                                        albumId = id,
                                        viewModel = albumDetailViewModel,
                                        navController = navController
                                    )
                                }
                                composable(
                                    "artist_detail/{id}",
                                    arguments = listOf(navArgument("id") { type = NavType.StringType }),
                                    enterTransition = {
                                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(400))
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
                                    )
                                }
                                composable(
                                    "reu_detail/{id}",
                                    arguments = listOf(
                                        navArgument("id") { type = NavType.StringType }
                                    ),
                                    enterTransition = {
                                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(400))
                                    },
                                    exitTransition = {
                                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)
                                    },
                                    popEnterTransition = { fadeIn() }
                                ) {
                                    val id = it.arguments?.getString("id")!!
                                    ReuDetailScreen(
                                        reuId = id,
                                        viewModel = reuDetailScreenViewModel,
                                        navController = navController,
                                        commonViewModel = commonViewModel)
                                }
                                composable(
                                    "web_view/{query}",
                                    arguments = listOf(
                                        navArgument("query") { type = NavType.StringType }
                                    ),
                                    enterTransition = {
                                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(400))
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
                        }
                    }
                }
            }
        }
    }
}

sealed class ItemsBottomNav(
    val icon: ImageVector,
    val title: String,
    val route: String
) {
    data object ItemDiscover : ItemsBottomNav(
        Icons.Default.Home,
        "Descubrir",
        "home"
    )
    data object ItemSearch : ItemsBottomNav(
        Icons.Default.Search,
        "Buscar",
        "search"
    )
    data object ItemReus : ItemsBottomNav(
        Icons.Default.AccountBox,
        "Reus",
        "reus"
    )
}

@Composable
fun BottomNav(navController: NavController) {
    val items = listOf(
        ItemsBottomNav.ItemDiscover,
        ItemsBottomNav.ItemSearch,
        ItemsBottomNav.ItemReus
    )
    BottomAppBar {
        NavigationBar {
            items.forEach {
                NavigationBarItem(
                    selected = currentRoute(navController = navController) == it.route,
                    onClick = { navController.navigate(it.route) },
                    icon = { Icon(imageVector = it.icon, contentDescription = "") },
                    label = { Text(text = it.title) }
                )
            }
        }
    }
}

@Composable
fun currentRoute(navController: NavController): String? = navController.currentBackStackEntryAsState().value?.destination?.route