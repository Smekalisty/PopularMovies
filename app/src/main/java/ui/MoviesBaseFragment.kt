package ui

import activities.MovieDetailsActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.popularmovies.R
import entities.pojo.Movie
import entities.helpers.SpaceItemDecoration
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.layout_movies.*
import java.net.UnknownHostException

abstract class MoviesBaseFragment : Fragment() {
    private var refresh: SwipeRefreshLayout? = null
    private var infoLayout: View? = null
    protected var disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_movies, container, false)

        activity?.title = getString(getTitle())

        infoLayout = view.findViewById(R.id.infoLayout)
        infoLayout?.visibility = View.GONE

        refresh = view.findViewById(R.id.refresh)
        refresh?.isRefreshing = true
        refresh?.setOnRefreshListener {
            requestDataSource()
        }

        val clickSubject = PublishSubject.create<Movie>()
        val disposable = clickSubject.subscribe(::onSelected, ::onError)
        disposables.add(disposable)

        view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            addItemDecoration(SpaceItemDecoration(view.context))
            layoutManager = LinearLayoutManager(view.context)
            setAdapter(this)
        }

        requestDataSource()

        return view
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    abstract fun getTitle(): Int

    abstract fun setAdapter(recyclerView: RecyclerView)

    abstract fun requestDataSource()

    abstract fun startMovieDetailsActivity(intent: Intent)

    protected fun getClickSubject(): PublishSubject<Movie> {
        val clickSubject = PublishSubject.create<Movie>()
        val disposable = clickSubject.subscribe(::onSelected, ::onError)
        disposables.add(disposable)
        return clickSubject
    }

    protected fun onDataSourceLoaded(hasItems: Boolean) {
        refresh?.isRefreshing = false

        if (hasItems) {
            infoLayout?.visibility = View.GONE
        } else {
            infoLayout?.visibility = View.VISIBLE
        }
    }

    protected fun onError(error: Throwable) {
        val itemCount = recyclerView?.adapter?.itemCount
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

    private fun showMessage(message: String) = view?.apply {
        Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun onSelected(movie: Movie) {
        val context = context ?: return
        val intent = Intent(context, MovieDetailsActivity::class.java)
        intent.putExtra(MovieDetailsActivity.extraMovie, movie)
        startMovieDetailsActivity(intent)
    }
}