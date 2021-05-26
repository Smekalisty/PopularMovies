package ui.tabs.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import constants.WebConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ui.tabs.pojo.Movie
import utils.Preference
import ui.tabs.pojo.MovieDetails
import utils.ResultWrapper
import utils.RetrofitManager

class MoviesFavoriteViewModel(application: Application) : AndroidViewModel(application) {
    var isReloadDataSourceRequired = false
    var favoritesSize = 0

    var dataSource: MutableList<MovieDetails>? = null

    fun requestDataSource(onSuccess: (MutableList<MovieDetails>) -> Unit, onError: (Throwable) -> Unit) {
        //TODO rx to coroutine

        /*disposable = Single.fromCallable { Preference.getFavoriteMovies(getApplication()) }
            .compose(RxManager.singleTransformer())
            .subscribe(onSuccess, onError)*/
    }
}