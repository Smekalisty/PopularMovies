package ui.main.favorite

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import entities.pojo.Movie
import entities.pojo.MovieDetails
import io.reactivex.subjects.PublishSubject
import ui.main.base.MovieAdapterExecutor
import ui.main.base.ViewHolder

class Adapter(private val clickSubject: PublishSubject<Movie>) : ListAdapter<MovieDetails, ViewHolder>(config) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return MovieAdapterExecutor().onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val movie = getItem(position) ?: return
        MovieAdapterExecutor().onBindViewHolder(viewHolder, movie, clickSubject)
    }

    companion object {
        private val config = object : DiffUtil.ItemCallback<MovieDetails>() {
            override fun areItemsTheSame(old: MovieDetails, new: MovieDetails) = old.id == new.id
            override fun areContentsTheSame(old: MovieDetails, new: MovieDetails) = true
        }
    }
}