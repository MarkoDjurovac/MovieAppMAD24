package com.example.movieappmad24.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappmad24.data.MovieRepository
import com.example.movieappmad24.models.MovieWithImages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class WatchlistScreenViewModel(
    private val repository: MovieRepository
) : ViewModel(), MovieViewModel {
    private val _favoriteMovies = MutableStateFlow(listOf<MovieWithImages>())
    val favoriteMovies: StateFlow<List<MovieWithImages>> = _favoriteMovies.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getFavoriteMovies().distinctUntilChanged()
                .collect{ listOfMovies ->
                    _favoriteMovies.value = listOfMovies
                }
        }
    }

    override fun toggleFavoriteMovie(movieId: Long) {
        viewModelScope.launch {
            val movie = _favoriteMovies.value.find { it.movie.movieId == movieId }
            movie?.let {
                it.movie.isFavorite = !it.movie.isFavorite
                repository.updateMovie(it.movie)
            }
        }
    }
}