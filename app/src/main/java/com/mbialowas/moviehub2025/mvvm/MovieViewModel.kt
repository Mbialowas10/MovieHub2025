package com.mbialowas.moviehub2025.mvvm

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbialowas.moviehub2025.BuildConfig
import com.mbialowas.moviehub2025.api.Api
import com.mbialowas.moviehub2025.api.db.AppDatabase
import com.mbialowas.moviehub2025.api.model.Movie
import com.mbialowas.moviehub2025.api.model.MovieData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieViewModel : ViewModel() {

    // api key
    var api_key: String = BuildConfig.TMDB_API_KEY
    //search term
    val searchTerm = mutableStateOf("")

    // movies list
    val movies = mutableStateOf<List<Movie>>(emptyList())

    // variable to keep track of the Movie Icon state
    private val _movieIconState = MutableStateFlow<Map<Int,Boolean>>(emptyMap())
    val movieIconState = _movieIconState.asStateFlow()


    fun searchMovies(movieName:String, database: AppDatabase){

        // api call
        val service = Api.retrofitService.searchMovieByName(api_key, movieName)

        service.enqueue(object : Callback<MovieData> {
            override fun onResponse(call: Call<MovieData>, response: Response<MovieData>) {
                Log.i("SearchDta","testing api call")
                movies.value = response.body()?.results ?: emptyList()
                Log.i("SearchData", movies.value.toString())
                GlobalScope.launch{
                    database.movieDao().insertAll(movies.value)
                }

            }

            override fun onFailure(
                call: Call<MovieData>,
                t: Throwable
            ) {
              Log.d("Error", "${t.message}")
            }

        })





    }


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
