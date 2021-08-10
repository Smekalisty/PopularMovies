package ui.tabs.popular.entities

import androidx.paging.PagingSource
import androidx.paging.PagingState
import constants.WebConstants
import database.master.Movie
import utils.RetrofitManager

class MoviesDataSource : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val popularMovies = RetrofitManager.backendService.requestPopularMovies(page, WebConstants.apiKey)
            val results = popularMovies.results

            val newResults = results.map {
                Movie(it.id, it.title, it.overview, it.voteCount, it.voteAverage, it.backdropPath)
            } //TODO refactor

            LoadResult.Page(newResults, null, page + 1)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}