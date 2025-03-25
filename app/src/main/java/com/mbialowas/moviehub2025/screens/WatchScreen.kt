package com.mbialowas.moviehub2025.screens

import android.util.Log
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
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.mbialowas.moviehub2025.api.model.Movie


@Composable
fun WatchScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
){
    var data by remember { mutableStateOf< List<Movie> >( emptyList() ) }

    var movie_avg_vote: Any? = null
    var movie_id: Any? = null
    var movie_overview: Any? = null
    var movie_popularity: Any? = null
    var movie_poster_path: Any? = null
    var movie_release_date: Any? = null
    var movie_title: Any? = null
    var movie_vote_count: Any? = null
    var isFavorite: Any? = null

    // reference to the movie collection
    val collectionReference = FirebaseFirestore.getInstance().collection("movies")
    var Movie = Movie()

    collectionReference
        .get()
        .addOnSuccessListener { documents ->
            val dataList = documents.map{documentSnapshot ->
                val dataMap = documentSnapshot.data // data from the firebase collection
                // converting the firestore database JSON object to a Movie object
                Movie(
                    id = dataMap["movie_id"] as? Int,
                    overview = dataMap["movie_overview"] as? String,
                    voteAverage = dataMap["movie_vote_average"] as? Double,
                    voteCount = dataMap["movie_vote_count"] as? Int,
                    popularity = dataMap["movie_popularity"] as? Double,
                    poster_path = dataMap["movie_poster_path"] as? String,
                    releaseDate = dataMap["movie_release_date"] as? String,
                    title = dataMap["movie_title"] as? String,
                    isFavorite = dataMap["isFavorite"] as? Boolean ?: false
                )
            }
            data = dataList
        }
        .addOnFailureListener { exception ->
            // Handle failure
            Log.e("Firestore Response", "Error getting documents: $exception")
        }
        Column{
            Text(text="Watch later Screen")
            LazyColumn{
                 items(data){ movie->
                     MovieCard(movie, navController)
                 }
             }

        }

}