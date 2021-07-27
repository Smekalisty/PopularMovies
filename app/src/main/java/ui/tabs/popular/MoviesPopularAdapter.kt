package ui.tabs.popular

import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import com.popularmovies.R
import ui.tabs.base.ViewHolder
import ui.tabs.pojo.Movie
import ui.tabs.base.MovieAdapterExecutor
import ui.tabs.popular.entities.MovieComparator

class MoviesPopularAdapter : PagingDataAdapter<Movie, ViewHolder>(MovieComparator) {
    var canAnimate = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return MovieAdapterExecutor().onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val movie = getItem(position) ?: return

        if (canAnimate) {
            val itemView = viewHolder.itemView
            itemView.animation = AnimationUtils.loadAnimation(itemView.context, R.anim.animation_rotate_in)
        }

        MovieAdapterExecutor().onBindViewHolder(viewHolder, movie)
    }
}