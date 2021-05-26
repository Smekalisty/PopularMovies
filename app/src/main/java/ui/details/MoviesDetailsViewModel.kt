package ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import constants.WebConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ui.tabs.pojo.Movie
import ui.tabs.pojo.MovieDetails
import utils.ResultWrapper
import utils.RetrofitManager

class MoviesDetailsViewModel : ViewModel() {
    fun requestMovieDetails(movie: Movie, callback: (ResultWrapper<MovieDetails>) -> Unit) {
        viewModelScope.launch {
            val result = requestMovieDetails(movie)
            callback(result)
        }
    }

    private suspend fun requestMovieDetails(movie: Movie): ResultWrapper<MovieDetails> {
        return withContext(Dispatchers.IO) {
            try {
                val movieDetails = RetrofitManager()
                    .getWebAPI()
                    .requestMovieDetails(movie.id, WebConstants.apiKey)

                ResultWrapper(movieDetails, null)
            } catch (e: Exception) {
                e.printStackTrace()
                ResultWrapper(null, e)
            }
        }
    }
}