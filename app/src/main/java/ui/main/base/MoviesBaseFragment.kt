package ui.main.base

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.popularmovies.R
import entities.pojo.Movie
import entities.helpers.SpaceItemDecoration
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import ui.details.MovieDetailsFragment
import java.net.UnknownHostException

abstract class MoviesBaseFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var refresh: SwipeRefreshLayout? = null
    private var infoLayout: View? = null
    private var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_movies, container, false)

        infoLayout = view.findViewById(R.id.infoLayout)
        infoLayout?.visibility = View.GONE

        refresh = view.findViewById(R.id.refresh)
        refresh?.isRefreshing = true
        refresh?.setOnRefreshListener {
            requestDataSource()
        }

        val clickSubject = PublishSubject.create<Pair<View, Movie>>()
        val disposable = clickSubject.subscribe(::onSelected, ::onError)
        disposables.add(disposable)

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            addItemDecoration(SpaceItemDecoration(view.context))
            layoutManager = LinearLayoutManager(view.context)
            setAdapter(this)
        }

        requestDataSource()

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    abstract fun setAdapter(recyclerView: RecyclerView)

    abstract fun requestDataSource()

    protected fun getClickSubject(): PublishSubject<Pair<View, Movie>> {
        val clickSubject = PublishSubject.create<Pair<View, Movie>>()
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

    private fun onSelected(pair: Pair<View, Movie>) {
        val detailsDirection = object : NavDirections {
            override fun getActionId(): Int {
                return R.id.main_to_details
            }

            override fun getArguments(): Bundle {
                val bundle = Bundle()
                bundle.putParcelable(MovieDetailsFragment.extraMovie, pair.second)
                return bundle
            }
        }

        val extras = FragmentNavigatorExtras(pair.first to "image_${pair.second.id}")

        findNavController().navigate(detailsDirection, extras)
    }
}