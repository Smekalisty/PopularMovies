package ui.tabs.popular.entities

import android.os.Looper
import androidx.paging.PagingSource
import androidx.paging.PagingState
import constants.WebConstants
import contracts.BackendService
import ui.tabs.pojo.Movie

class MoviesDataSource(private val backendService: BackendService) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1

            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                println("qwerty is UI thread")
            } else {
                println("qwerty is not UI thread")
            }

            println("qwerty loading page $page")

            val result = backendService.requestPopularMovies(page, WebConstants.apiKey)
            LoadResult.Page(result, null, page + 1)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}