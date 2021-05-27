package ui.tabs.favorite

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import ui.tabs.pojo.MovieDetails
import ui.tabs.base.MoviesBaseFragment
import utils.ResultWrapper

class MoviesFavoriteFragment : MoviesBaseFragment() {
    private val viewModel by activityViewModels<MoviesFavoriteViewModel>()

    private var adapter: MoviesFavoriteAdapter? = null

    override fun setAdapter(recyclerView: RecyclerView) {
        adapter = MoviesFavoriteAdapter()
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun requestDataSource(force: Boolean) {
        when {
            viewModel.isReloadDataSourceRequired -> {
                viewModel.isReloadDataSourceRequired = false
                viewModel.requestDataSource(::onRequestDataSourceDone)
            }
            viewModel.dataSource == null -> {
                viewModel.requestDataSource(::onRequestDataSourceDone)
            }
            else -> {
                onRequestDataSourceDone(ResultWrapper(viewModel.dataSource!!, null))
            }
        }
    }

    private fun onRequestDataSourceDone(result: ResultWrapper<MutableList<MovieDetails>>) {
        if (result.payload == null) {
            onError(result.error!!)
        } else {
            onDataSourceLoaded(result.payload.isNotEmpty())
            adapter?.submitList(result.payload)
        }
    }
}