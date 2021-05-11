package ui.tabs.popular

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import ui.tabs.base.ViewHolder
import ui.tabs.pojo.Movie
import ui.tabs.base.MovieAdapterExecutor
import ui.tabs.popular.entities.MovieComparator

class MoviesPopularAdapter : PagingDataAdapter<Movie, ViewHolder>(MovieComparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return MovieAdapterExecutor().onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val movie = getItem(position) ?: return
        MovieAdapterExecutor().onBindViewHolder(viewHolder, movie)
    }
}