package ui.tabs.popular

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import database.master.Movie
import ui.tabs.base.MovieFavoriteDiff
import ui.tabs.base.ViewHolder

class MoviesPopularAdapter : PagingDataAdapter<Movie, ViewHolder>(MovieFavoriteDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.factory(parent)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val movie = getItem(position) ?: return
        viewHolder.onBindViewHolder(movie)
    }
}