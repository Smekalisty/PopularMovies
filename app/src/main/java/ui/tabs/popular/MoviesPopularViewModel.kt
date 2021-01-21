package ui.tabs.popular

import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.google.gson.GsonBuilder
import contracts.WebAPI
import ui.tabs.popular.entities.DataSourceFactory
import ui.tabs.popular.entities.MovieJsonDeserializer
import constants.WebConstants
import utils.RetrofitManager
import ui.tabs.pojo.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MoviesPopularViewModel : ViewModel() {
    private val webAPI: WebAPI

    private val disposables = CompositeDisposable()

    init {
        val gson = GsonBuilder()
            .registerTypeAdapter(List::class.java, MovieJsonDeserializer())
            .create()

        webAPI = RetrofitManager().getWebAPI(gson)
    }

    var dataSource: PagedList<Movie>? = null

    fun loadDataSource(onPagedListReady: (PagedList<Movie>) -> Unit, onSuccess: (List<Movie>) -> Unit, onError: (Throwable) -> Unit) {
        val dataSourceFactory = DataSourceFactory(webAPI, disposables, onSuccess, onError)

        val disposable = RxPagedListBuilder(dataSourceFactory, WebConstants.pageSize)
            .buildObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(onPagedListReady, onError)

        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}