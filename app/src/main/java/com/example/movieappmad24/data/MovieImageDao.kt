package com.example.movieappmad24.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.movieappmad24.models.MovieImage

@Dao
interface MovieImageDao {
    @Insert
    suspend fun add(image: MovieImage)

    @Insert
    suspend fun addAll(images: List<MovieImage>)

    @Update
    suspend fun update(image: MovieImage)

    @Delete
    suspend fun delete(image: MovieImage)

}