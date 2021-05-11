package ui.tabs.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.gson.GsonBuilder
import constants.WebConstants
import contracts.BackendService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ui.tabs.pojo.Movie
import ui.tabs.popular.entities.MovieJsonDeserializer
import ui.tabs.popular.entities.MoviesDataSource
import utils.RetrofitManager

class MoviesPopularViewModel : ViewModel() {
    private val backendService: BackendService

    private var dataSource: PagingData<Movie>? = null

    private var adapter: MoviesPopularAdapter? = null

    init {
        val gson = GsonBuilder()
            .registerTypeAdapter(List::class.java, MovieJsonDeserializer())
            .create()

        backendService = RetrofitManager().getWebAPI(gson)
    }

    fun requestDataSource(adapter: MoviesPopularAdapter?) {
        this.adapter = adapter

        viewModelScope.launch {
            if (dataSource == null) {
                println("qwerty requestDataSource from network")

                requestDataSource().collect(::onDataSourceLoaded)
            } else {
                println("qwerty requestDataSource from cache")
                adapter?.submitData(dataSource!!)
            }
        }
    }

    private fun requestDataSource(): Flow<PagingData<Movie>> {
        println("qwerty requestDataSource 2")

        val source = {
            MoviesDataSource(backendService)
        }

        val flow = Pager(PagingConfig(pageSize = WebConstants.pageSize), null, source).flow.cachedIn(viewModelScope)
        return flow
    }

    private suspend fun onDataSourceLoaded(dataSource: PagingData<Movie>) {
        this.dataSource = dataSource
        adapter?.submitData(dataSource)
    }
}