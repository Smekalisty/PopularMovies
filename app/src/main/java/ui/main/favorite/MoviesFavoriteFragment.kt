package ui.main.favorite

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import entities.pojo.MovieDetails
import ui.main.base.MoviesBaseFragment

class MoviesFavoriteFragment : MoviesBaseFragment() {
    private val viewModel by activityViewModels<MoviesFavoriteViewModel>()

    private var adapter: MoviesFavoriteAdapter? = null

    override fun setAdapter(recyclerView: RecyclerView) {
        adapter = MoviesFavoriteAdapter(getClickSubject())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun requestDataSource() {
        when {
            viewModel.isReloadDataSourceRequired -> {
                viewModel.isReloadDataSourceRequired = false
                viewModel.requestDataSource(::onDataSourceLoaded, ::onError)
            }
            viewModel.dataSource == null -> {
                viewModel.requestDataSource(::onDataSourceLoaded, ::onError)
            }
            else -> {
                onDataSourceLoaded(viewModel.dataSource!!)
            }
        }
    }

    private fun onDataSourceLoaded(dataSource: MutableList<MovieDetails>) {
        viewModel.dataSource = dataSource
        onDataSourceLoaded(dataSource.isNotEmpty())
        adapter?.submitList(dataSource)
    }
}