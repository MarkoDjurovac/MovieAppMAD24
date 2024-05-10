package com.example.movieappmad24.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.movieappmad24.models.Movie
import com.example.movieappmad24.models.MovieWithImages
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert
    suspend fun add(movie: Movie): Long

    @Update
    suspend fun update(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("SELECT * FROM movie WHERE movieId =:id")
    fun get(id: Long): Flow<Movie>

    @Transaction
    @Query("SELECT * FROM movie")
    fun getAll(): Flow<List<MovieWithImages>>

    @Transaction
    @Query("SELECT * FROM movie WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<MovieWithImages>>

    @Transaction
    @Query("SELECT * FROM movie WHERE movieId =:id")
    fun getWithImages(id: Long): Flow<MovieWithImages>
}