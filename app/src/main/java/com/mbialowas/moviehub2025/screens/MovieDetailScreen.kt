package com.mbialowas.moviehub2025.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.mbialowas.moviehub2025.api.MoviesManager
import com.mbialowas.moviehub2025.api.db.AppDatabase
import com.mbialowas.moviehub2025.api.model.Movie
import com.mbialowas.moviehub2025.mvvm.MovieViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MovieDetailScreen(
    movie: Movie,
    modifier: Modifier,
    moviesManager: MoviesManager,
    db: AppDatabase,
    viewModel: MovieViewModel
){
    // state level variables
    var showDialog by remember {mutableStateOf(false)}
    var showEditDialog by remember {mutableStateOf(false)}

    var isIconChanged by remember { mutableStateOf(viewModel.movieIconState.value[movie.id] ?: false) }

    movie.originalTitle?.let { Log.i("Movie", it)}
    Box(
        modifier = Modifier
            .fillMaxSize() ,
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .background(
                    color = Color.DarkGray
                )
        ) {
            movie.overview?.let { Log.i("Movie", movie.overview!!) }
            Text(
                modifier = Modifier
                    .background(color = Color.Black)
                    .padding(1.dp, 5.dp, 1.dp, 1.dp)
                    .fillMaxWidth(),
                text = "Movie Detail Screen",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Box{


                AsyncImage(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f),
                    model =  ImageRequest.Builder(
                        LocalContext.current
                    ).data("https://image.tmdb.org/t/p/w500/${movie.poster_path}")
                        .build(),
                    contentDescription = movie.overview,
                    contentScale = ContentScale.FillBounds
                )
                Button(
                    onClick = {
                        isIconChanged = !isIconChanged // toggle the icon
                        viewModel.updateMovieIconState(movie.id!!, db)
                        Log.i("Button", "Button Clicked")
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ){
                    Icon(
                        imageVector = if (isIconChanged) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Watch",
                        tint = Color.White
                    )
                }
                Row(
                    modifier
                        .align(Alignment.BottomEnd)
                ){
                    Button(
                        onClick = {
                            showEditDialog = true // show the editDialog
                        }
                    ) {
                        // trigger dialog if true
                        Text(text = "Edit")
                    }
                    Button(
                        onClick = {
                            showDialog = true
                        }
                    ) {
                        // trigger dialog if true
                        Text(text = "Delete")
                    }
                    // show DeleteMovieDialog when showDialog is true
                    if (showDialog) {
                        DeleteMovieDialog(
                            movie = movie,
                            onDismiss = { showDialog = false },
                            onConfirmDelete = {
                                // delete the movie
                                CoroutineScope(Dispatchers.IO).launch {
                                    // call our call dao delete function
                                    db.movieDoa().deleteMovie(movie)

                                    // refresh the movie screen
                                    moviesManager.refreshMovies()
                                }
                                showDialog = false
                            }
                        )
                    } // END showDialog
                    if (showEditDialog) {
                        EditMovieDialog(
                            movie = movie,
                            onDismiss = { showEditDialog = false },
                            onConfirmEdit = { newTitle, newDescription ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    movie.id?.let{
                                        // update movie in Database
                                        db.movieDoa().updateMovie(it, newTitle, newDescription)

                                        // refresh the list
                                        moviesManager.refreshMovies()
                                    }
                                    showEditDialog = false
                                }
                            }
                        )
                    }
                }

            } // end box

            Column(Modifier.padding(20.dp)){
                Spacer(modifier = Modifier.padding(5.dp))
                movie.releaseDate?.let {
                    Text(
                        text= "Release Date: $it",
                        modifier = Modifier.padding(end=5.dp),
                        maxLines = 1,
                        fontSize = 12.sp,
                        overflow= TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge,
                        color=Color.White
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    movie.overview?.let {
                        Text(
                            text= it,
                            modifier = Modifier.padding(end=8.dp),
                            maxLines = 3,
                            fontSize = 16.sp,
                            overflow= TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium,
                            color=Color.White
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row{
                        movie.voteAverage?.let{
                            Text(
                                text= "Avg Vote: $it",
                                modifier = Modifier.padding(end=8.dp),
                                maxLines = 1,
                                fontSize = 20.sp,
                                overflow= TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.labelLarge,
                                color=Color.White
                            )
                        }
                        movie.voteCount?.let{
                            Text(
                                text= "# of votes: $it",
                                modifier = Modifier.padding(end=8.dp),
                                maxLines = 1,
                                fontSize = 20.sp,
                                overflow= TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.labelLarge,
                                color=Color.White
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

}