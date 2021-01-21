package ui.tabs.popular.entities

import androidx.paging.DataSource
import contracts.WebAPI
import ui.tabs.pojo.Movie
import io.reactivex.disposables.CompositeDisposable

class DataSourceFactory(private val webAPI: WebAPI, private val disposables: CompositeDisposable, private val onSuccess: (List<Movie>) -> Unit, private val onError: (Throwable) -> Unit) : DataSource.Factory<Int, Movie>() {
    override fun create() = MoviesDataSource(webAPI, disposables, onSuccess, onError)
}