package com.mbialowas.moviehub2025.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.mbialowas.moviehub2025.api.MoviesManager

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
    }
}