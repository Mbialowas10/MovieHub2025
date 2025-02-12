package com.mbialowas.moviehub2025.destinations

/*
 Where the app should go!
 SEALED so that we limite the number of subclasses that can
 be dervied
 */

sealed class Destination(val route: String) {
    object Movie :  Destination("movie")
    object Watch :  Destination("watch")
    object Search : Destination("search")

    object MovieDetail: Destination("movieDetail/{movieID}"){
        //fun createRoute(movieID: Int?) = "movieDetail/$movieID"
    }
}