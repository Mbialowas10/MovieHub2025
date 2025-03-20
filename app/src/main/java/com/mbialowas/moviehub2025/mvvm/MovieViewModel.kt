package com.mbialowas.moviehub2025.mvvm

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mbialowas.moviehub2025.api.db.AppDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll

class MovieViewModel : ViewModel() {

    // variable to keep track of the Movie Icon state
    var movieIconState = mutableStateOf<Map<Int,Boolean>>(emptyMap())

   /*
    * Purpose - a function to update the movie favortie state ie. true or false
    * @params movieId: Int - represent the movieId
    * @params database: AppDatabase - represent the database
    * @return unit
    */
   @OptIn(DelicateCoroutinesApi::class)
   fun updateMovieIconState(movieId: Int, database: AppDatabase) {
        GlobalScope.launch {
            database.movieDao().getMovieById(movieId)?.let {  movie ->
                movie.isFavorite = !movie.isFavorite

                database.movieDao().updateMovieState(movie)

                // update state in viewModel
                movieIconState.value = movieIconState.value.toMutableMap().apply {
                    this[movieId] = !movie.isFavorite
                }
            }
        }
    }
}