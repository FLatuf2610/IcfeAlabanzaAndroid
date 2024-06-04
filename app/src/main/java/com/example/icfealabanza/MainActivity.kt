package com.example.icfealabanza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.icfealabanza.presentation.home.HomeScreen
import com.example.icfealabanza.presentation.home.HomeViewModel
import com.example.icfealabanza.presentation.search.SearchScreen
import com.example.icfealabanza.presentation.search.SearchViewModel
import com.example.icfealabanza.ui.theme.IcfeAlabanzaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val homeViewModel: HomeViewModel = hiltViewModel()
            val searchViewModel: SearchViewModel = hiltViewModel()
            IcfeAlabanzaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   NavHost(navController = navController, startDestination = "search") {
                       composable("home") {
                           HomeScreen(viewModel = homeViewModel, navController = navController)
                       }
                       composable("search") {
                           SearchScreen(viewModel = searchViewModel, navController = navController)
                       }
                       composable(
                           "album_detail/{id}",
                           arguments = listOf(navArgument("id") { type = NavType.IntType })
                       ) {
                           val id = it.arguments?.getInt("id",0)

                       }
                       composable(
                           "artist_detail/{id}",
                           arguments = listOf(navArgument("id") { type = NavType.IntType })
                           ) {
                            val id = it.arguments?.getInt("id", 0)

                       }
                       composable(
                           "song_detail/{id}",
                           arguments = listOf(navArgument("id") { type = NavType.IntType })
                       ) {
                           val id = it.arguments?.getInt("id", 0)

                       }
                       composable(
                           "web_view/{query}/{action}",
                           arguments = listOf(
                               navArgument("query") { type = NavType.StringType },
                               navArgument("action") { type = NavType.StringType }
                           )
                       ) {
                            val query = it.arguments?.getString("query")
                            val action = it.arguments?.getString("action")

                       }
                   }
                }
            }
        }
    }
}
