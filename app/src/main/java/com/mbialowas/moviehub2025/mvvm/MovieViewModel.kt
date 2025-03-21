package com.mbialowas.moviehub2025.mvvm

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbialowas.moviehub2025.api.db.AppDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import kotlinx.coroutines.Dispatchers

class MovieViewModel : ViewModel() {

    // variable to keep track of the Movie Icon state
    private val _movieIconState = MutableStateFlow<Map<Int,Boolean>>(emptyMap())
    val movieIconState = _movieIconState.asStateFlow()

   /**
    * Purpose - a function to update the movie favorite state ie. true or false
    * @params movieId: Int - represent the movieId
    * @params database: AppDatabase - represent the database
    * @return unit
    */

   fun updateMovieIconState(movieId: Int, database: AppDatabase) {
       viewModelScope.launch(Dispatchers.IO) {
           val movie = database.movieDao().getMovieById(movieId)

           if ( movie != null ){
               movie.isFavorite = !movie.isFavorite

               // update movie in the database
               launch(Dispatchers.IO){
                   database.movieDao().updateMovieState(movie)

                   _movieIconState.value = _movieIconState.value.toMutableMap().apply{
                       this[movieId] = movie.isFavorite
                   }
               }

           }else {
               Log.e("MovieViewModel", "Movie with ID $movieId not found in database")
           }
       }

   }
}
