package ui.tabs.popular

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.popularmovies.R
import ui.tabs.popular.entities.ProgressViewHolder

class MoviesPopularStateAdapter : LoadStateAdapter<ProgressViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ProgressViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.layout_progress, parent, false)
        return ProgressViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProgressViewHolder, loadState: LoadState) { }
}