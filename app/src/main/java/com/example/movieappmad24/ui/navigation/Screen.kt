package com.example.movieappmad24.ui.navigation

const val DETAIL_ARGUMENT_KEY = "movieId"
sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home")
    data object DetailScreen : Screen("detail/{$DETAIL_ARGUMENT_KEY}") {
        fun route(movieId: Long): String {
            return this.route.replace(oldValue = "{$DETAIL_ARGUMENT_KEY}", newValue = movieId.toString())
        }
    }
    data object WatchlistScreen : Screen("watchlist")
}