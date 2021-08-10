package ui.tabs.pojo

import kotlinx.serialization.*

@Serializable
data class MovieEntity(
    val id: Int = 0,
    val title: String = "",
    val overview: String? = null,
    @SerialName("vote_count") val voteCount: Long = 0,
    @SerialName("vote_average") val voteAverage: Float = 0f,
    @SerialName("backdrop_path") val backdropPath: String? = null
)