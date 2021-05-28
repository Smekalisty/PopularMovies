package ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import constants.WebConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ui.tabs.pojo.Movie
import ui.tabs.pojo.MovieDetails
import utils.RetrofitManager

class MoviesDetailsViewModel : ViewModel() {
    private val mutableFlow = MutableSharedFlow<Result<MovieDetails>>()
    val flow: SharedFlow<Result<MovieDetails>> = mutableFlow.asSharedFlow()

    fun requestDataSource(movie: Movie) {
        viewModelScope.launch {
            val result = loadMovieDetails(movie)
            mutableFlow.emit(result)
        }
    }

    private suspend fun loadMovieDetails(movie: Movie): Result<MovieDetails> {
        return withContext(Dispatchers.IO) {
            try {
                val movieDetails = RetrofitManager()
                    .getWebAPI()
                    .requestMovieDetails(movie.id, WebConstants.apiKey)

                Result.success(movieDetails)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}