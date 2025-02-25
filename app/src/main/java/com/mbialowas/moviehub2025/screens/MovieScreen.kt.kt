package com.mbialowas.moviehub2025.screens

import android.provider.Settings.Global
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.mbialowas.moviehub2025.api.MoviesManager
import com.mbialowas.moviehub2025.api.db.AppDatabase
import com.mbialowas.moviehub2025.api.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun MovieScreen(navController: NavHostController, modifier: Modifier = Modifier, moviesManager: MoviesManager,db: AppDatabase){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
    ){
        Text(
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = modifier.align(Alignment.Center),
            text="Movie Screen"
        )
        val movies = moviesManager.moviesResponse.value // observe movie list

        LazyColumn{
            items(movies){movie ->
                MovieCard(movieItem = movie, navController = navController,db,moviesManager)
                Log.i("HTTP:", "https://image.tmdb.org/t/p/w500${movie.poster_path }")
            }
        }

    }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun MovieCard(
    movieItem: Movie,
    navController: NavHostController,
    db: AppDatabase,
    moviesManager: MoviesManager
) {

    var showDialog by remember { mutableStateOf(false) } // Manage dialog state


    Column(
        modifier = Modifier
            .border(1.dp, Color.Red, shape = RoundedCornerShape(10.dp))
            .padding(5.dp)
            .clickable {
                Log.i("MovieCard", "Clicked ${movieItem.title}")
                Log.i("MovieCard", "Clicked ${movieItem.id}")
                navController.navigate("movieDetail/${movieItem.id}")
            }
    ){
        Row(
            modifier = Modifier
                .background(Color.DarkGray)
                .fillMaxWidth()
                .padding(5.dp)
        ){
            // import another library
            AsyncImage(
                model = ImageRequest.Builder(
                    LocalContext.current
                ).data("https://image.tmdb.org/t/p/w500${movieItem.poster_path}")
                    .build(),
                contentDescription = movieItem.overview
            )
            // add in edit and delete btns
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Edit")
            }
            Button(onClick = {
               showDialog = true // trigger dialog
            }) {
                Text(text = "Delete")
            }

        }
    }
    // Show DeleteMovieDialog when showDialog is true
    if (showDialog) {
        DeleteMovieDialog(
            movie = movieItem,
            onDismiss = { showDialog = false },
            onConfirmDelete = {
                CoroutineScope(GlobalScope.coroutineContext).launch {
                    db.movieDoa().deleteMovieById(movieItem) // Use correct function call
                    // refresh movies
                    moviesManager.refreshMovies()
                }
                showDialog = false
            }
        )
    }

}
@Composable
fun DeleteMovieDialog(
    movie: Movie?,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    if (movie != null) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "Delete Movie") },
            text = { Text("Are you sure you want to delete the movie \"${movie.title}\"?") },
            confirmButton = {
                TextButton(onClick = { onConfirmDelete() }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        )
    }
}


