package ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import constants.WebConstants
import database.details.MovieDetails
import database.master.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                val movieDetails = RetrofitManager.backendService.requestMovieDetails(movie.id, WebConstants.apiKey)

                val movieDetails1 = MovieDetails(
                    movieDetails.id,
                    movieDetails.budget,
                    movieDetails.homepage,
                    movieDetails.tagLine,
                    movieDetails.releaseDate,
                    movieDetails.posterPath
                ) //TODO refactor

                Result.success(movieDetails1)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}