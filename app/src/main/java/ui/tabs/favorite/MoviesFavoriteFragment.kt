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
                viewModel.requestDataSource(::onDataSourceLoaded)
            }
            viewModel.dataSource == null -> {
                viewModel.requestDataSource(::onDataSourceLoaded)
            }
            else -> {
                onDataSourceLoaded(ResultWrapper(viewModel.dataSource!!, null))
            }
        }
    }

    //TODO сохрянять dataSource в view model-e а не тут

    private fun onDataSourceLoaded(result: ResultWrapper<MutableList<MovieDetails>>) {
        if (result.payload == null) {
            onError(result.error!!)
        } else {
            viewModel.dataSource = result.payload
            onDataSourceLoaded(result.payload.isNotEmpty())
            adapter?.submitList(result.payload)
        }
    }
}