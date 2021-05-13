package ui.tabs.popular

import android.view.View
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import ui.tabs.base.MoviesBaseFragment

class MoviesPopularFragment : MoviesBaseFragment() {
    private var adapter: MoviesPopularAdapter? = null

    private val viewModel by viewModels<MoviesPopularViewModel>()

    override fun setAdapter(recyclerView: RecyclerView) {
        adapter = MoviesPopularAdapter().apply {
            this.addLoadStateListener { state ->
                println("qwerty refresh=${state.refresh}")
                if (state.refresh == LoadState.Loading) {
                    binding?.refresh?.isRefreshing = true
                } else {
                    binding?.refresh?.isRefreshing = false
                }
            }
        }

        recyclerView.adapter = adapter
    }

    override fun requestDataSource(force: Boolean) {
        adapter?.let {
            viewModel.requestDataSource(force, it)
        }
    }
}