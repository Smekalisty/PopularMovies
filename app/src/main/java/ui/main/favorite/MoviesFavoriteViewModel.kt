package ui.main.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import entities.helpers.Preference
import entities.helpers.RxManager
import entities.pojo.MovieDetails
import io.reactivex.Single
import io.reactivex.disposables.Disposable

class MoviesFavoriteViewModel(application: Application) : AndroidViewModel(application) {
    var isReloadDataSourceRequired = false

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