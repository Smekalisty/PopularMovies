package ui.tabs.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import ui.tabs.pojo.MovieDetails
import ui.tabs.base.MoviesBaseFragment

class MoviesFavoriteFragment : MoviesBaseFragment() {
    private val viewModel by activityViewModels<MoviesFavoriteViewModel>()

    private var adapter: MoviesFavoriteAdapter? = null

    override fun setAdapter(recyclerView: RecyclerView) {
        adapter = MoviesFavoriteAdapter()
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenCreated {
            viewModel.flow
                .onEach(::requestDataSourceDone)
                .collect()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun requestDataSource(force: Boolean) {
        when {
            viewModel.isReloadDataSourceRequired -> {
                viewModel.isReloadDataSourceRequired = false
                viewModel.requestDataSource()
            }
            viewModel.dataSource == null -> {
                viewModel.requestDataSource()
            }
            else -> {
                requestDataSourceSuccess(viewModel.dataSource!!)
            }
        }
    }

    private fun requestDataSourceDone(result: Result<MutableList<MovieDetails>>) {
        result.fold(::requestDataSourceSuccess, ::onError)
    }

    private fun requestDataSourceSuccess(dataSource: MutableList<MovieDetails>) {
        onDataSourceLoaded(dataSource.isNotEmpty())
        adapter?.submitList(dataSource)
    }
}