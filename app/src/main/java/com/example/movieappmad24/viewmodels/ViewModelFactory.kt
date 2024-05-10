package com.example.movieappmad24.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieappmad24.data.MovieRepository

class ViewModelFactory(
    private val repository: MovieRepository,
    private val movieId: Long? = null): ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeScreenViewModel::class.java) -> HomeScreenViewModel(repository) as T
            modelClass.isAssignableFrom(WatchlistScreenViewModel::class.java) -> WatchlistScreenViewModel(repository) as T
            modelClass.isAssignableFrom(DetailScreenViewModel::class.java) -> DetailScreenViewModel(repository, movieId!!) as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}