package com.mbialowas.moviehub2025.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.mbialowas.moviehub2025.api.model.Movie

@Composable
fun MovieDetailScreen(modifier: Modifier, movie: Movie){
    Box(
        modifier
            .background(Color.Cyan)
            .fillMaxSize()
    ){
        Log.i("MovieDetailScreen", "${movie.title}")
        Log.i("MovieDetailScreen", "${movie.overview}")
        Log.i("MovieDetailScreen", "${movie.poster_path}")

        Column{
            Text(
                text = movie.title!!,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = movie.overview!!,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = movie.poster_path!!,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}