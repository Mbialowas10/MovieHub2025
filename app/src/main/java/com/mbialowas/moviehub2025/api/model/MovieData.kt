package com.mbialowas.moviehub2025.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieData(
    @Json(name = "page")
    var page: Int?,
    @Json(name = "results")
    var results: List<Movie?>?
)