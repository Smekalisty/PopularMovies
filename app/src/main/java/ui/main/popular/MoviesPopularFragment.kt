package ui.main.popular

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import entities.pojo.Movie
import ui.main.base.MoviesBaseFragment

class MoviesPopularFragment : MoviesBaseFragment() {
    private var adapter: MoviesPopularAdapter? = null

    private val viewModel by viewModels<MoviesPopularViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.fade)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setAdapter(recyclerView: RecyclerView) {
        adapter = MoviesPopularAdapter(getClickSubject())
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