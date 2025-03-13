package com.mbialowas.moviehub2025.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.mbialowas.moviehub2025.api.MoviesManager
import com.mbialowas.moviehub2025.api.db.AppDatabase
import com.mbialowas.moviehub2025.api.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MovieScreen(navController: NavHostController, modifier: Modifier = Modifier, moviesManager: MoviesManager, db: AppDatabase){
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
        val movies = moviesManager.moviesResponse.value
        LazyColumn{
            items(movies){movie ->
                MovieCard(movieItem = movie, navController = navController, db = db, moviesManager)
                Log.i("HTTP:", "https://image.tmdb.org/t/p/w500${movie.poster_path }")
            }
        }

    }
}

@Composable
fun MovieCard(
    movieItem: Movie,
    navController: NavHostController,
    db: AppDatabase,
    moviesManager: MoviesManager
) {
    var showDialog by remember { mutableStateOf(false)}
    var showEditDialog by remember { mutableStateOf(false)}
    Column(
        modifier = Modifier
            .border(1.dp,Color.Red,shape= RoundedCornerShape(10.dp))
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
            Button(
                onClick = {
                    // call a showEditDialog function
                    showEditDialog = true
                }
            ){
               Text(text="Edit")
            }
            Button(
                onClick = {
                    // call a showDeleteDialog function
                    showDialog = true
                }
            ) {
                Text(text="Delete")
            }
        }
        // show DeleteDialog when is true
        if (showDialog){
            DeleteMovieDialog(
                movie = movieItem,
                onDismiss = { showDialog = false },
                onConfirmDelete = {
                    // call a deleteMovie function
                    CoroutineScope(Dispatchers.IO).launch {
                        db.movieDao().delete(movieItem)

                        // refresh the list
                        moviesManager.refreshMovies()
                    }
                    showDialog = false
                }
            )

        } // END DELETE DIALOG
        if (showEditDialog) {
            EditMovieDialog(
                movie = movieItem,
                onDismiss = { showEditDialog = false },
                onConfirmEdit = { newTitle, newDescription ->
                    // call a editMovie function
                    CoroutineScope(Dispatchers.IO).launch {
                        // update the movie in our database
                        movieItem.id?.let {
                            db.movieDao().updateMovie(it, newTitle, newDescription)
                        }
                        showEditDialog = false

                    }
                }
            )
        }

    }
}

@Composable
fun DeleteMovieDialog(
    movie: Movie?,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
if (movie != null){
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Delete Movie") },
        text = { Text(text = "Are you sure you want to delete ${movie.title}?") },
        confirmButton = {
            TextButton( onClick = { onConfirmDelete() } )
            {
                Text(text="Delete",color=Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() })
            {
                Text(text = "Cancel", color = Color.Green)
            }
        }
    )

    }
} // END DELETE MOVIE DIALOG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMovieDialog(
    movie: Movie?,
    onDismiss: () -> Unit,
    onConfirmEdit: (String, String) -> Unit // Pass new title & description as parameters
){
    if (movie != null) {
        var newTitle by remember { mutableStateOf(movie.title) }
        var newDescription by remember { mutableStateOf(movie.overview) }

        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "Edit Movie") },
            text = {
                Column {
                    Text(text = "Update movie details:")
                    Spacer(modifier = Modifier.height(8.dp))

                    // title input field
                    newTitle?.let {
                        OutlinedTextField(
                            value = it,
                            onValueChange = { newTitle = it },
                            label = { Text(text = "Title") },
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    newDescription?.let {
                        OutlinedTextField(
                            value = it,
                            onValueChange = { newDescription = it },
                            label = { Text(text = "Description") },
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    newTitle?.let {
                        newDescription?.let { it1 ->
                            onConfirmEdit(
                                it,
                                it1
                            )
                        }
                    }
                }) {
                    Text("Save", color = Color.Green)
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("Cancel", color = Color.Red)
                }
            }
        ) // END AlertDialog
    }
}


