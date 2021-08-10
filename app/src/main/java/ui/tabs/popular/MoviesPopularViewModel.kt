package ui.tabs.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import constants.WebConstants
import database.master.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ui.tabs.popular.entities.MoviesDataSource

class MoviesPopularViewModel : ViewModel() {
    private var flowDataSource: Flow<PagingData<Movie>>

    init {
        flowDataSource = createDataSourceFlow()
    }

    private fun createDataSourceFlow(): Flow<PagingData<Movie>> {
        val source = {
            MoviesDataSource()
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