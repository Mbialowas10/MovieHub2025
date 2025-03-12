package com.mbialowas.moviehub2025.mvvm

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.room.Database
import com.mbialowas.moviehub2025.BuildConfig
import com.mbialowas.moviehub2025.api.Api
import com.mbialowas.moviehub2025.api.db.AppDatabase
import com.mbialowas.moviehub2025.api.model.Movie
import com.mbialowas.moviehub2025.api.model.MovieData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {

    var api_key:String = BuildConfig.TMDB_API_KEY

    val movies = mutableStateOf<List<Movie>>(emptyList())

    // search term
    val searchTerm = mutableStateOf("")

    @OptIn(DelicateCoroutinesApi::class)
    fun searchMovies(movieName:String, database: AppDatabase){
        // api call
        val service = Api.retrofitService.searchMovieByName(api_key, movieName)
        
        service.enqueue(object : Callback<MovieData> {
            override fun onResponse(
                call: Call<MovieData>,
                response: Response<MovieData>
            ) {
                if (response.isSuccessful) {
                    Log.i("SearchData", "testing api call")
                    movies.value = response.body()?.results ?: emptyList()
                    Log.i("MovieFound", movies.toString())
                    GlobalScope.launch {
                        database.movieDoa().insertAllMovies(movies.value)
                    }
                }
            }

            override fun onFailure(
                call: Call<MovieData>,
                t: Throwable
            ) {
                Log.d("Error", "${t.message}")
            }

        })
            

    } // end search Movies

    //save searched term
    fun saveSearchTerm(term:String){
        searchTerm.value = term
    }
}