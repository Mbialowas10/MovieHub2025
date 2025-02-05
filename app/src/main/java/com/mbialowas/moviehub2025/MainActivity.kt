package com.mbialowas.moviehub2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.mbialowas.moviehub2025.Navigation.BottomNav
import com.mbialowas.moviehub2025.destinations.Destination
import com.mbialowas.moviehub2025.screens.MovieScreen
import com.mbialowas.moviehub2025.screens.*
import com.mbialowas.moviehub2025.ui.theme.MovieHub2025Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieHub2025Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    //MovieScreen(modifier = Modifier.padding(innerPadding))
                    //WatchScreen(modifier = Modifier.padding(innerPadding))
                    App(navController = navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(navController: NavHostController, modifier: Modifier = Modifier){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MovieHub 2025") }
            )
        },
        bottomBar = { BottomNav(navController = navController) }
    ){ paddingValues ->
        paddingValues.calculateBottomPadding()
        Spacer(modifier = Modifier.padding(paddingValues))
        NavHost(
            navController = navController as NavHostController, startDestination = Destination.Movie.route
        ){
            composable(Destination.Movie.route){
                MovieScreen()
            }
            composable(Destination.Watch.route) {
                WatchScreen()
            }
            composable(Destination.Search.route) {
                SearchScreen()
            }
        }

    }
}

