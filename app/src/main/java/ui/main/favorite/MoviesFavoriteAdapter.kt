package ui.main.favorite

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import entities.pojo.MovieDetails
import ui.main.base.MovieAdapterExecutor
import ui.main.base.ViewHolder

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