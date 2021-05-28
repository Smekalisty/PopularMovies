package ui.tabs.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.Preference
import ui.tabs.pojo.MovieDetails

class MoviesFavoriteViewModel(application: Application) : AndroidViewModel(application) {
    var isReloadDataSourceRequired = false
    var favoritesSize = 0

    //TODO save inside flow like padding??? cacheIn
    var dataSource: MutableList<MovieDetails>? = null

    private val mutableFlow = MutableSharedFlow<Result<MutableList<MovieDetails>>>()
    val flow: SharedFlow<Result<MutableList<MovieDetails>>> = mutableFlow.asSharedFlow()

    fun requestDataSource() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val favoriteMovies = Preference.getFavoriteMovies(getApplication())
                    Result.success(favoriteMovies)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }

            val dataSource = result.getOrNull()
            if (dataSource != null) {
                this@MoviesFavoriteViewModel.dataSource = dataSource
            }

            mutableFlow.emit(result)
        }
    }
}