package com.mbialowas.moviehub2025

import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.mbialowas.moviehub2025.Navigation.BottomNav
import com.mbialowas.moviehub2025.api.MoviesManager
import com.mbialowas.moviehub2025.api.db.AppDatabase
import com.mbialowas.moviehub2025.api.model.Movie
import com.mbialowas.moviehub2025.destinations.Destination
import com.mbialowas.moviehub2025.screens.MovieScreen
import com.mbialowas.moviehub2025.screens.*
import com.mbialowas.moviehub2025.ui.theme.MovieHub2025Theme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieHub2025Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    // get db instance
                    val db = AppDatabase.getInstance(applicationContext)
                    val moviesManager = MoviesManager(db)


                    App(navController = navController, modifier = Modifier.padding(innerPadding), moviesManager, db)
                }
            }
        }
    }
}
@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(navController: NavHostController, modifier: Modifier = Modifier, moviesManager: MoviesManager, db:AppDatabase){
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
                MovieScreen(navController = navController, moviesManager = moviesManager, db = db)
            }
            composable(Destination.Watch.route) {
                WatchScreen()
            }
            composable(Destination.Search.route) {
                SearchScreen()
            }
            composable(Destination.MovieDetail.route){ navBackStackEntry ->
                var movie by remember {
                    mutableStateOf<Movie?>(null)
                }
                //val movie = Movie(title="Fake Movie", overview="This is a much better movie than Wicked!", poster_path = "fake.png")
                val movie_id: String? = navBackStackEntry.arguments?.getString("movieID")
                // monitor GlobalScope annotation if code breaks in future see - @SuppressLint("CoroutineCreationDuringComposition
                GlobalScope.launch {
                    if (movie_id != null) {
                        movie = db.movieDoa().getMovieById( movie_id.toInt() )
                    }
                }
                movie?.let{
                    MovieDetailScreen(modifier = Modifier.padding(paddingValues), movie= movie!!)
                }

                //MovieDetailScreen(modifier = Modifier.padding(paddingValues), movie=movie)

            }
        }

    }
}

