package ui.tabs.favorite

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ui.tabs.pojo.MovieDetails
import ui.tabs.base.MovieAdapterExecutor
import ui.tabs.base.ViewHolder

class MoviesFavoriteAdapter : ListAdapter<MovieDetails, ViewHolder>(config) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return MovieAdapterExecutor().onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val movie = getItem(position) ?: return
        MovieAdapterExecutor().onBindViewHolder(viewHolder, movie)
    }

    companion object {
        private val config = object : DiffUtil.ItemCallback<MovieDetails>() {
            override fun areItemsTheSame(old: MovieDetails, new: MovieDetails) = old.id == new.id
            override fun areContentsTheSame(old: MovieDetails, new: MovieDetails) = true
        }
    }
}