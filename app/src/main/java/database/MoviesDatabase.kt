package database

import androidx.room.Database
import androidx.room.RoomDatabase
import database.details.MovieDetails
import database.details.MovieDetailsDao
import database.master.Movie
import database.master.MoviesDao

@Database(entities = [Movie::class, MovieDetails::class], version = 1, exportSchema = false)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
    abstract fun movieDetailsDao(): MovieDetailsDao
}