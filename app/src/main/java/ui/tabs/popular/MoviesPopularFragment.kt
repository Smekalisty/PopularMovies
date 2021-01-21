package ui.tabs.popular

import androidx.fragment.app.viewModels
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import ui.tabs.pojo.Movie
import ui.tabs.base.MoviesBaseFragment

class MoviesPopularFragment : MoviesBaseFragment() {
    private var adapter: MoviesPopularAdapter? = null

    private val viewModel by viewModels<MoviesPopularViewModel>()

    override fun setAdapter(recyclerView: RecyclerView) {
        adapter = MoviesPopularAdapter()
        recyclerView.adapter = adapter
    }

    override fun requestDataSource() {
        if (viewModel.dataSource != null) {
            onPagedListReady(viewModel.dataSource!!)
        } else {
            viewModel.loadDataSource(::onPagedListReady, ::onInitialDataSourceLoaded, ::onError)
        }
    }

    private fun onPagedListReady(dataSource: PagedList<Movie>) {
        viewModel.dataSource = dataSource
        adapter?.submitList(dataSource)
    }

    private fun onInitialDataSourceLoaded(dataSource: List<Movie>) {
        onDataSourceLoaded(dataSource.isNotEmpty())
    }
}