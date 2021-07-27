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
    var runAnimationOnce = true

    private val backendService: BackendService

    private var flowDataSource: Flow<PagingData<Movie>>

    init {
        val gson = GsonBuilder()
            .registerTypeAdapter(List::class.java, MovieJsonDeserializer())
            .create()

        backendService = RetrofitManager().getWebAPI(gson)

        flowDataSource = createDataSourceFlow()
    }

    private fun createDataSourceFlow(): Flow<PagingData<Movie>> {
        val source = {
            MoviesDataSource(backendService)
        }

        return Pager(PagingConfig(pageSize = WebConstants.pageSize), null, source)
            .flow
            .cachedIn(viewModelScope)
    }

    fun requestDataSource(force: Boolean, adapter: MoviesPopularAdapter) {
        if (force) {
            flowDataSource = createDataSourceFlow()
        }

        viewModelScope.launch {
            flowDataSource.collect(adapter::submitData)
        }
    }
}