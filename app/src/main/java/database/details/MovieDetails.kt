package database.details

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class MovieDetails(
    @PrimaryKey val id: Int = 0,
    val budget: Int = 0,
    val homepage: String? = null,
    val tagLine: String = "",
    val releaseDate: String = "",
    val posterPath: String? = null
) : Parcelable