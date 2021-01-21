package contracts

import io.reactivex.Single
import ui.tabs.pojo.Movie
import ui.tabs.pojo.MovieDetails
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WebAPI {
    @POST("3/movie/popular")
    fun requestPopularMovies(@Query("page") page: Int, @Query("api_key") accessToken: String): Single<List<Movie>>

    @GET("3/movie/{id}")
    fun requestMovieDetails(@Path("id") id: Int, @Query("api_key") accessToken: String): Single<MovieDetails>
}