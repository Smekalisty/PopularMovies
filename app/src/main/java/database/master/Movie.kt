package database.master

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Movie(
    @PrimaryKey val id: Int = 0,
    val title: String = "",
    val overview: String? = null,
    val voteCount: Long = 0,
    val voteAverage: Float = 0f,
    val backdropPath: String? = null
) : Parcelable