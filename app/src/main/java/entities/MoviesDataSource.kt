package entities

import androidx.paging.PageKeyedDataSource
import contracts.WebAPI
import entities.helpers.RxManager
import entities.pojo.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviesDataSource(private val webAPI: WebAPI, private val disposables: CompositeDisposable, private val onSuccess: (List<Movie>) -> Unit,  private val onError: (Throwable) -> Unit) : PageKeyedDataSource<Int, Movie>() {
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        val disposable = webAPI
            .requestPopularMovies(1, WebConstants.apiKey)
            .compose(RxManager.singleTransformer())
            .subscribe({
                callback.onResult(it, 1, 2)
                onSuccess(it)
            }, onError)

        disposables.add(disposable)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        val disposable = webAPI
            .requestPopularMovies(params.key, WebConstants.apiKey)
            .compose(RxManager.singleTransformer())
            .subscribe({ callback.onResult(it, params.key + 1) }, onError)

        disposables.add(disposable)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) { }
}