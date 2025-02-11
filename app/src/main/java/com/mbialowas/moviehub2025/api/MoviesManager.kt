package com.mbialowas.moviehub2025.api

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mbialowas.moviehub2025.api.model.Movie
import com.mbialowas.moviehub2025.api.model.MovieData
import retrofit2.Call
import retrofit2.Response

class MoviesManager {
    private var _moviesResponse = mutableStateOf<List<Movie>>(emptyList())

    val api_key = "aaed4e12019db7b90c9cebd9c1082790"
    val moviesResponse: MutableState<List<Movie>>
        @Composable get() = remember {
            _moviesResponse
        }
    init{
        getMovies()
    }
    private fun  getMovies(){
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

                }
            }

            override fun onFailure(call: Call<MovieData>, t: Throwable) {
                Log.d("error", "${t.message}")
            }

        }

        )
    }
}