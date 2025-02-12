package com.mbialowas.moviehub2025.api

import com.mbialowas.moviehub2025.api.model.MovieData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesService {

    // endpoints get defined here
    @GET("trending/movie/day")
    fun getTrendingMovies(@Query("api_key") apiKey: String): Call<MovieData>
}