package contracts

import ui.tabs.pojo.Movie
import ui.tabs.pojo.MovieDetails
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BackendService {
    @POST("3/movie/popular")
    suspend fun requestPopularMovies(@Query("page") page: Int, @Query("api_key") accessToken: String): List<Movie>

    @GET("3/movie/{id}")
    suspend fun requestMovieDetails(@Path("id") id: Int, @Query("api_key") accessToken: String): MovieDetails
}