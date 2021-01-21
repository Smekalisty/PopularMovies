package ui.tabs.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import utils.Preference
import utils.RxManager
import ui.tabs.pojo.MovieDetails
import io.reactivex.Single
import io.reactivex.disposables.Disposable

class MoviesFavoriteViewModel(application: Application) : AndroidViewModel(application) {
    var isReloadDataSourceRequired = false
    var favoritesSize = 0

    private var disposable: Disposable? = null

    var dataSource: MutableList<MovieDetails>? = null

    fun requestDataSource(onSuccess: (MutableList<MovieDetails>) -> Unit, onError: (Throwable) -> Unit) {
        disposable = Single.fromCallable { Preference.getFavoriteMovies(getApplication()) }
            .compose(RxManager.singleTransformer())
            .subscribe(onSuccess, onError)
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}