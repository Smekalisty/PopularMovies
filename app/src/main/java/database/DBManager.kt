package database

import android.content.Context
import androidx.room.Room

object DBManager {
    private const val databaseName = "moviesDatabase"

    private var moviesDatabase: MoviesDatabase? = null

    fun getInstance(context: Context): MoviesDatabase {
        if (moviesDatabase == null) {
            moviesDatabase = Room.databaseBuilder(context, MoviesDatabase::class.java, databaseName)
                .fallbackToDestructiveMigration()
                .build()
        }

        return moviesDatabase!!
    }
}