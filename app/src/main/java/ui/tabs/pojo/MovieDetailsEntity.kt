package ui.tabs.pojo

import kotlinx.serialization.*

@Serializable
data class MovieDetailsEntity(
    val id: Int = 0,
    val budget: Int = 0,
    val homepage: String? = null,
    @SerialName("tag_line") val tagLine: String = "",
    @SerialName("release_date") val releaseDate: String = "",
    @SerialName("poster_path") val posterPath: String? = null
)