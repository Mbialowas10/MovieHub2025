package com.mbialowas.moviehub2025.api

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mbialowas.moviehub2025.BuildConfig
import com.mbialowas.moviehub2025.api.db.AppDatabase
import com.mbialowas.moviehub2025.api.model.Movie
import com.mbialowas.moviehub2025.api.model.MovieData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class MoviesManager(database: AppDatabase) {
    private var _moviesResponse = mutableStateOf<List<Movie>>(emptyList())

    //val api_key = "aaed4e12019db7b90c9cebd9c1082790"
    // now import api key from gradle.properties
    private val api_key = BuildConfig.TMDB_API_KEY

    val moviesResponse: MutableState<List<Movie>>
        @Composable get() = remember {
            _moviesResponse
        }
    private val db = database
    init{
        getMovies(db)
    }
    private fun  getMovies(database:AppDatabase){
        Log.i("api_key", api_key)
        val service = Api.retrofitService.getTrendingMovies(api_key)

        service.enqueue(object : retrofit2.Callback<MovieData>{
            override fun onResponse(
                call: Call<MovieData>,
                response: Response<MovieData>
            ) {
                if (response.isSuccessful) {
                    Log.i("Data", "Data is loaded")

                    _moviesResponse.value = response.body()?.results ?: emptyList()
                    Log.i("DataStream", _moviesResponse.value.toString())

                    GlobalScope.launch {
                        saveDataToDatabase(database = database, _moviesResponse.value)
                    }

                }
            }

            override fun onFailure(call: Call<MovieData>, t: Throwable) {
                Log.d("error", "${t.message}")
            }

        }

        )
    }

    private suspend fun saveDataToDatabase(database: AppDatabase, movies: List<Movie>) {
        database.movieDoa().insertAllMovies(movies)
    }

    suspend fun refreshMovies(){
        var movies = db.movieDoa().getAllMovies()
        _moviesResponse.value = movies

    }
}