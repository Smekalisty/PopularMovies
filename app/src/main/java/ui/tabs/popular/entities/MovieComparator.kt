package ui.tabs.popular.entities

import androidx.recyclerview.widget.DiffUtil
import ui.tabs.pojo.Movie

object MovieComparator : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(old: Movie, new: Movie): Boolean {
        return old.id == new.id
    }

    override fun areContentsTheSame(old: Movie, new: Movie): Boolean {
        return old.hashCode() == new.hashCode()
    }
}