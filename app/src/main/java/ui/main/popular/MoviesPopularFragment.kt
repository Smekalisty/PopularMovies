package ui.main.popular

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.popularmovies.R
import entities.MoviesViewModel
import entities.pojo.Movie
import ui.main.base.MoviesBaseFragment
import java.util.*

class MoviesPopularFragment : MoviesBaseFragment() {
    private var adapter: Adapter? = null

    override fun getTitle() = R.string.popular_movies

    override fun setAdapter(recyclerView: RecyclerView) {
        adapter = Adapter(getClickSubject())
        recyclerView.adapter = adapter
    }

    private val viewModel by viewModels<MoviesViewModel>()

    override fun requestDataSource() {
        if (viewModel.dataSource != null) {
            onPagedListReady(viewModel.dataSource!!)
        } else {
            viewModel.loadDataSource(::onPagedListReady, ::onInitialDataSourceLoaded, ::onError)
        }
    }

    override fun showTopLevelFragment(view: View, bundle: Bundle) {
        val nd = object : NavDirections {
            override fun getActionId(): Int {
                return R.id.master_to_details
            }

            override fun getArguments(): Bundle {
                return bundle
            }
        }

        val transitionName = UUID.randomUUID().toString()
        view.transitionName = transitionName
        val extras = FragmentNavigatorExtras(view to view.transitionName)

        findNavController().navigate(nd, extras)
    }

    private fun onPagedListReady(dataSource: PagedList<Movie>) {
        viewModel.dataSource = dataSource
        adapter?.submitList(dataSource)
    }

    private fun onInitialDataSourceLoaded(dataSource: List<Movie>) {
        onDataSourceLoaded(dataSource.isNotEmpty())
    }
}