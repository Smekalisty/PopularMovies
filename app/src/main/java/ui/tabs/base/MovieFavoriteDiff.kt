package ui.tabs.base

import androidx.recyclerview.widget.DiffUtil
import database.master.Movie

class MovieFavoriteDiff : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(old: Movie, new: Movie) = old.id == new.id
    override fun areContentsTheSame(old: Movie, new: Movie) = false
}