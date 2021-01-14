package ui.main.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.popularmovies.R
import entities.pojo.MovieDetails
import ui.main.base.MoviesBaseFragment

class MoviesFavoriteFragment : MoviesBaseFragment() {
    private val viewModel by activityViewModels<MoviesFavoriteViewModel>()

    private var adapter: Adapter? = null

    override fun setAdapter(recyclerView: RecyclerView) {
        adapter = Adapter(getClickSubject())
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

    override fun showTopLevelFragment(view: View, bundle: Bundle) {
        findNavController().navigate(R.id.main_to_details, bundle)
    }

    private fun onDataSourceLoaded(dataSource: MutableList<MovieDetails>) {
        viewModel.dataSource = dataSource
        onDataSourceLoaded(dataSource.isNotEmpty())
        adapter?.submitList(dataSource)
    }
}