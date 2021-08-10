package ui.tabs.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import database.DBManager
import database.master.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesFavoriteViewModel(application: Application) : AndroidViewModel(application) {
    var isReloadDataSourceRequired = false
    var favoritesSize = 0

    //TODO save inside flow like padding??? cacheIn
    var dataSource: List<Movie>? = null

    private val mutableFlow = MutableSharedFlow<Result<List<Movie>>>()
    val flow: SharedFlow<Result<List<Movie>>> = mutableFlow.asSharedFlow()

    fun requestDataSource() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val favoriteMovies = DBManager.getInstance(getApplication())
                        .moviesDao()
                        .getAll()

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