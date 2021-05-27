package ui.tabs.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.Preference
import ui.tabs.pojo.MovieDetails
import utils.ResultWrapper

class MoviesFavoriteViewModel(application: Application) : AndroidViewModel(application) {
    var isReloadDataSourceRequired = false
    var favoritesSize = 0

    var dataSource: MutableList<MovieDetails>? = null

    fun requestDataSource(callback: (ResultWrapper<MutableList<MovieDetails>>) -> Unit) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val favoriteMovies = Preference.getFavoriteMovies(getApplication())
                    ResultWrapper(favoriteMovies, null)
                } catch (e: Exception) {
                    ResultWrapper<MutableList<MovieDetails>>(null, e)
                }
            }

            callback(result)
        }
    }
}