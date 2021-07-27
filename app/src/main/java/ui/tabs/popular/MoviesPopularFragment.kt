package ui.tabs.popular

import android.view.ViewTreeObserver
import androidx.fragment.app.viewModels
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ui.tabs.base.MoviesBaseFragment

class MoviesPopularFragment : MoviesBaseFragment() {
    private var adapter: MoviesPopularAdapter? = null

    private val viewModel by viewModels<MoviesPopularViewModel>()

    override fun setAdapter(recyclerView: RecyclerView) {
        adapter = MoviesPopularAdapter().apply {
            addLoadStateListener(::onStateChanged)
            val stateAdapter = withLoadStateFooter(MoviesPopularStateAdapter())
            recyclerView.adapter = ConcatAdapter(this, stateAdapter)
        }

        if (viewModel.runAnimationOnce) {
            viewModel.runAnimationOnce = false
            adapter?.canAnimate = true
            recyclerView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    val lastPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (lastPosition > 0) {
                        adapter?.canAnimate = false
                        recyclerView.viewTreeObserver.removeOnPreDrawListener(this)
                    }
                    return true
                }
            })
        }
    }

    override fun requestDataSource(force: Boolean) {
        adapter?.let {
            viewModel.requestDataSource(force, it)
        }
    }

    private fun onStateChanged(state: CombinedLoadStates) {
        binding?.let {
            val refreshState = state.refresh
            if (refreshState is LoadState.Error) {
                Snackbar.make(it.root, refreshState.error.message.toString(), Snackbar.LENGTH_SHORT).show()
            }

            it.refresh.isRefreshing = state.refresh == LoadState.Loading
        }
    }
}