package com.mbialowas.moviehub2025.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.media3.common.util.Log
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.mbialowas.moviehub2025.api.MoviesManager
import com.mbialowas.moviehub2025.api.db.AppDatabase
import com.mbialowas.moviehub2025.api.model.Movie

@Composable
fun WatchScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    moviesManager: MoviesManager,
    db: AppDatabase
){
    var data by remember { mutableStateOf <List<Movie>>(emptyList()) }

    // reference the movies collection
    val collectionReference = FirebaseFirestore.getInstance().collection("movies")
    var movie = Movie()

    collectionReference
        .get()
        .addOnSuccessListener { documents ->
            val dataList = documents.map { documentSnapshot ->
                val dataMap = documentSnapshot.data
                Movie(
                    id = (dataMap["movie_id"] as? Number)?.toInt(), // Safely cast
                    overview = dataMap["movie_overview"] as? String,
                    voteAverage = (dataMap["movie_avg_vote"] as? Number)?.toDouble(),
                    popularity = (dataMap["movie_popularity"] as? Number)?.toDouble(),
                    poster_path = dataMap["movie_poster_path"] as? String,
                    releaseDate = dataMap["movie_release_date"] as? String,
                    title = dataMap["movie_title"] as? String,
                )
            }
            data = dataList
        }
        .addOnFailureListener { exception ->
            Log.d("Firestore Response", "Error gettings documents: ", exception)
        }
    Column{
        Text(text = "Watch Later Screen")

        LazyColumn {
           items(data){ movie ->
               Log.i("Movie", movie.toString())
               MovieCard(movie, navController, db, moviesManager)
           }
        }
    }


}