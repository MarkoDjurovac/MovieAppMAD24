package com.example.movieappmad24.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.movieappmad24.data.MovieDatabase
import com.example.movieappmad24.data.MovieRepository
import com.example.movieappmad24.viewmodels.ViewModelFactory
import com.example.movieappmad24.viewmodels.WatchlistScreenViewModel
import com.example.movieappmad24.ui.widgets.MovieList
import com.example.movieappmad24.ui.widgets.SimpleBottomAppBar
import com.example.movieappmad24.ui.widgets.SimpleTopAppBar

@Composable
fun WatchlistScreen(navController: NavController){
    val db = MovieDatabase.getDatabase(LocalContext.current)
    val repository = MovieRepository(movieDao = db.movieDao(), movieImageDao = db.movieImageDao())
    val factory = ViewModelFactory(repository = repository)
    val viewModel: WatchlistScreenViewModel = viewModel(factory = factory)

    val favoriteMovies by viewModel.favoriteMovies.collectAsState()

    Scaffold (
        topBar = {
            SimpleTopAppBar(title = "Your Watchlist")
        },
        bottomBar = {
            SimpleBottomAppBar(
                navController = navController
            )
        }
    ){ innerPadding ->
        MovieList(
            modifier = Modifier.padding(innerPadding),
            movies = favoriteMovies,
            navController = navController,
            viewModel = viewModel
        )
    }
}