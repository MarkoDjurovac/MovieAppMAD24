package com.example.movieappmad24.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movieappmad24.models.Movie
import com.example.movieappmad24.models.MovieImage
import com.example.movieappmad24.models.getMovies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Movie::class, MovieImage::class],  // tables in the db
    version = 15,                // schema version; whenever you change schema you have to increase the version number
    exportSchema = false        // for schema version history updates
)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao // Dao instance so that the DB knows about the Dao
    // add more daos here if you have multiple tables
    abstract fun movieImageDao(): MovieImageDao

    // declare as singleton - companion objects are like static variables in Java
    companion object{
        @Volatile   // never cache the value of instance
        private var instance: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase{
            return instance ?: synchronized(this) { // wrap in synchronized block to prevent race conditions
                Room.databaseBuilder(context, MovieDatabase::class.java, "movie_db")
                    .fallbackToDestructiveMigration() // if schema changes wipe the whole db - there are better migration strategies for production usage
                    .build() // create an instance of the db
                    .also {
                        instance = it   // override the instance with newly created db
                        CoroutineScope(Dispatchers.IO).launch {
                            seedDatabase(it)
                        }
                    }
            }
        }

        private fun seedDatabase(database: MovieDatabase) {
            CoroutineScope(Dispatchers.IO).launch {
                val seed = getMovies()
                seed.forEach {movieWithImages ->
                    val movie = movieWithImages.movie
                    val images = movieWithImages.movieImages
                    val movieId = database.movieDao().add(movie)
                    images.forEach { image ->
                        image.movieId = movieId
                        database.movieImageDao().add(image)
                    }
                }
            }
        }
    }
}