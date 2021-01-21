package ui.tabs.popular

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import ui.tabs.base.ViewHolder
import ui.tabs.pojo.Movie
import ui.tabs.base.MovieAdapterExecutor

class MoviesPopularAdapter : PagedListAdapter<Movie, ViewHolder>(config) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return MovieAdapterExecutor().onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val movie = getItem(position) ?: return
        MovieAdapterExecutor().onBindViewHolder(viewHolder, movie)
    }

    companion object {
        private val config = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(old: Movie, new: Movie) = old.id == new.id
            override fun areContentsTheSame(old: Movie, new: Movie) = old.hashCode() == new.hashCode()
        }
    }
}