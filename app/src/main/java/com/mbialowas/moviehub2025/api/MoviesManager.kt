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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class MoviesManager(database:AppDatabase) {
    private var _moviesResponse = mutableStateOf<List<Movie>>(emptyList())
    private val db = database
    val api_key = BuildConfig.TMDB_API_KEY

    val moviesResponse: MutableState<List<Movie>>
        @Composable get() = remember {
            _moviesResponse
        }

    init{
        getMovies(db)
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun  getMovies(database: AppDatabase){
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

                    // save data to the database
                    GlobalScope.launch{
                        saveDataToDatabase(database = database, movies = _moviesResponse.value)
                    }

                }
            }

            override fun onFailure(call: Call<MovieData>, t: Throwable) {
                Log.d("error", "${t.message}")
            }

        }

        )
    }
    private suspend fun saveDataToDatabase(database: AppDatabase, movies: List<Movie>){
        database.movieDao().insertAll(movies)
    }

    suspend fun refreshMovies(){
        var movies = db.movieDao().getAllMovies()
        _moviesResponse.value = movies
    }
}