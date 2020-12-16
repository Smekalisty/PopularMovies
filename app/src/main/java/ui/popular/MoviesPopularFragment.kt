package ui.popular

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.popularmovies.R
import entities.MoviesViewModel
import entities.pojo.Movie
import ui.MoviesBaseFragment

class MoviesPopularFragment : MoviesBaseFragment() {
    private var adapter: Adapter? = null

    override fun getTitle() = R.string.popular_movies

    override fun setAdapter(recyclerView: RecyclerView) {
        adapter = Adapter(getClickSubject())
        recyclerView.adapter = adapter
    }

    override fun requestDataSource() {
        ViewModelProvider(this)
            .get(MoviesViewModel::class.java)
            .loadDataSource(::onPagedListReady, ::onInitialDataSourceLoaded, ::onError)
    }

    override fun startMovieDetailsActivity(intent: Intent) {
        startActivity(intent)
    }

    private fun onPagedListReady(dataSource: PagedList<Movie>) {
        adapter?.submitList(dataSource)
    }

    private fun onInitialDataSourceLoaded(dataSource: List<Movie>) {
        onDataSourceLoaded(dataSource.isNotEmpty())
    }
}