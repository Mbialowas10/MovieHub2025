package com.mbialowas.moviehub2025.destinations

sealed class Destination(val route: String) {
    object Movie : Destination("movie")
    object Search : Destination("search")
    object Watch : Destination("watch")

}