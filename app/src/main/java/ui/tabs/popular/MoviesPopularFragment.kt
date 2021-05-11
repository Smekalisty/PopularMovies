package ui.tabs.popular

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import ui.tabs.base.MoviesBaseFragment

class MoviesPopularFragment : MoviesBaseFragment() {
    private var adapter: MoviesPopularAdapter? = null

    private val viewModel by viewModels<MoviesPopularViewModel>()

    override fun setAdapter(recyclerView: RecyclerView) {
        adapter = MoviesPopularAdapter()
        recyclerView.adapter = adapter
    }

    override fun requestDataSource() {
        viewModel.requestDataSource(adapter)
    }
}