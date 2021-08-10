package database.details

import androidx.room.*

@Dao
interface MovieDetailsDao {
    @Query("SELECT * FROM movieDetails WHERE id = :id")
    suspend fun getMovieDetails(id: Int): MovieDetails?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieDetails: MovieDetails)

    @Delete
    suspend fun delete(movieDetails: MovieDetails)
}