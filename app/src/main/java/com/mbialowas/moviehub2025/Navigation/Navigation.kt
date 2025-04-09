package com.mbialowas.moviehub2025.Navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mbialowas.moviehub2025.R
import com.mbialowas.moviehub2025.destinations.Destination

@Composable
fun BottomNav(navController: NavController) {
    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination

        val ic_movie = painterResource(id = R.drawable.ic_movie)
        val ic_watch = painterResource(id = R.drawable.ic_watch)
        val ic_search = painterResource(id = R.drawable.ic_search)

        NavigationBarItem(
            selected = currentDestination?.route == Destination.Movie.route,
            onClick = { navController.navigate(Destination.Movie.route) {
                popUpTo(Destination.Movie.route)
                launchSingleTop = true
            }},
            icon = { Icon(painter = ic_movie, contentDescription = null)},
            label = { Text(text = Destination.Movie.route) }
        )// end movie
        NavigationBarItem(
            selected = currentDestination?.route == Destination.Search.route,
            onClick = { navController.navigate(Destination.Search.route) {
                popUpTo(Destination.Search.route)
                launchSingleTop = true
            }},
            icon = { Icon(painter = ic_search, contentDescription = null)},
            label = { Text(text = Destination.Search.route) }
        ) // end search
        NavigationBarItem(
            selected = currentDestination?.route == Destination.Watch.route,
            onClick = { navController.navigate(Destination.Watch.route) {
                popUpTo(Destination.Watch.route)
                launchSingleTop = true
            }},
            icon = { Icon(painter = ic_watch, contentDescription = null)},
            label = { Text(text = Destination.Watch.route) }
        ) // end watch
        NavigationBarItem(
            selected = currentDestination?.route == Destination.MapScreen.route,
            onClick = { navController.navigate(Destination.MapScreen.route) {
                popUpTo(Destination.MapScreen.route)
                launchSingleTop = true
            }},
            icon = { Icon(painter = ic_watch, contentDescription = null)},
            label = { Text(text = Destination.MapScreen.route) }
        ) // end watch
    }
}