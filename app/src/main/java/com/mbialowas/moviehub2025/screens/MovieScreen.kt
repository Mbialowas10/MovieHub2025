package com.mbialowas.moviehub2025.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.mbialowas.moviehub2025.api.MoviesManager
import com.mbialowas.moviehub2025.api.model.Movie

@Composable
fun MovieScreen(modifier: Modifier = Modifier, moviesManager: MoviesManager){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
    ){
        Text(
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign =  TextAlign.Center,
            modifier = Modifier.align(Alignment.Center),
            text="Movie Screen"
        )
        val movies = moviesManager.moviesResponse.value

        LazyColumn {
            items(movies){movie ->
                MovieCard(movieItem = movie)

            }
        }
    }
}

@Composable
fun MovieCard(
    movieItem: Movie
) {
    Column(
        modifier = Modifier
            .border(1.dp, Color.Red,shape= RoundedCornerShape(10.dp))
            .padding(5.dp)
    ) {
        Row(
           modifier = Modifier
               .fillMaxSize()
               .background(Color.DarkGray)
               .padding(5.dp)
        ){
            // bake in image
            AsyncImage(
                model = ImageRequest.Builder(
                    LocalContext.current
                ).data("https://image.tmdb.org/t/p/w500/${movieItem.posterPath}")
                    .build(),
            contentDescription = movieItem.overview)


        }
    }
}
