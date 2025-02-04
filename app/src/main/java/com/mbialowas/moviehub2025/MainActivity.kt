package com.mbialowas.moviehub2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                    //MovieScreen(modifier = Modifier.padding(innerPadding))
                    WatchScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

