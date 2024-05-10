package com.example.movieappmad24.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappmad24.data.MovieRepository
import com.example.movieappmad24.models.Movie
import com.example.movieappmad24.models.MovieWithImages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class DetailScreenViewModel(
    private val repository: MovieRepository,
    private val movieId: Long
) : ViewModel() {
    private val _movie = MutableStateFlow(MovieWithImages())
    val movie: StateFlow<MovieWithImages> = _movie.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getByIdWithImages(movieId).distinctUntilChanged().collect { movieWithImages ->
                if (movieWithImages != null) {
                    _movie.value = movieWithImages
                }
            }
        }
    }

    fun toggleFavoriteMovie() {
        viewModelScope.launch {
            repository.updateMovie(
                Movie(
                    _movie.value.movie.movieId,
                    _movie.value.movie.id,
                    _movie.value.movie.title,
                    _movie.value.movie.year,
                    _movie.value.movie.genre,
                    _movie.value.movie.director,
                    _movie.value.movie.actors,
                    _movie.value.movie.plot,
                    _movie.value.movie.trailer,
                    _movie.value.movie.rating,
                    !_movie.value.movie.isFavorite
                )
            )
        }
    }
}