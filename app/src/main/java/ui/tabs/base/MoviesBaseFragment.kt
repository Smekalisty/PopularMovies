package ui.tabs.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.popularmovies.R
import com.popularmovies.databinding.FragmentMoviesBinding
import java.net.UnknownHostException

abstract class MoviesBaseFragment : Fragment(R.layout.fragment_movies) {
    protected var binding: FragmentMoviesBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentMoviesBinding.bind(view)
        this.binding = binding

        with(binding) {
            infoLayout.visibility = View.GONE

            refresh.isRefreshing = true
            refresh.setOnRefreshListener {
                requestDataSource()
            }

            recyclerView.addItemDecoration(SpaceItemDecoration(view.context))
            recyclerView.layoutManager = LinearLayoutManager(view.context)
            setAdapter(recyclerView)
        }

        requestDataSource()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    abstract fun setAdapter(recyclerView: RecyclerView)

    abstract fun requestDataSource()

    protected fun onDataSourceLoaded(hasItems: Boolean) {
        binding?.let {
            it.refresh.isRefreshing = false

            if (hasItems) {
                it.infoLayout.visibility = View.GONE
            } else {
                it.infoLayout.visibility = View.VISIBLE
            }
        }
    }

    protected fun onError(error: Throwable) {
        val itemCount = binding?.recyclerView?.adapter?.itemCount
        val hasItems = if (itemCount == null) {
            false
        } else {
            itemCount > 0
        }

        onDataSourceLoaded(hasItems)

        if (error is UnknownHostException) {
            showMessage(getString(R.string.probably_no_connection))
        } else {
            val message = error.message ?: error.toString()
            showMessage(message)
        }
    }

    private fun showMessage(message: String) {
        binding?.let {
            Snackbar.make(it.root, message, Snackbar.LENGTH_SHORT).show()
        }
    }
}