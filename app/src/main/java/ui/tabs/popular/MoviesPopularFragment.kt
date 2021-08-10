package ui.tabs.popular

import androidx.fragment.app.viewModels
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ui.tabs.base.MoviesBaseFragment

class MoviesPopularFragment : MoviesBaseFragment() {
    private var adapter: MoviesPopularAdapter? = null

    private val viewModel by viewModels<MoviesPopularViewModel>()

    override fun setAdapter(recyclerView: RecyclerView) {
        adapter = MoviesPopularAdapter().apply {
            addLoadStateListener(::onStateChanged)
            val concatAdapter = withLoadStateFooter(MoviesPopularStateAdapter())
            recyclerView.adapter = concatAdapter
        }
    }

    override fun requestDataSource(force: Boolean) {
        adapter?.let {
            viewModel.requestDataSource(force, it)
        }
    }

    private fun onStateChanged(state: CombinedLoadStates) {
        binding?.let { binding ->
            binding.root.post {
                val refreshState = state.refresh
                if (refreshState is LoadState.Error) {
                    Snackbar.make(binding.root, refreshState.error.message.toString(), Snackbar.LENGTH_SHORT).show()
                }

                binding.refresh.isRefreshing = state.refresh == LoadState.Loading
            }
        }
    }
}