package ui.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.popularmovies.R
import entities.WebConstants
import entities.helpers.Preference
import entities.helpers.RetrofitManager
import entities.helpers.RxManager
import entities.helpers.Wrap
import entities.pojo.Movie
import entities.pojo.MovieDetails
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import ui.main.favorite.MoviesFavoriteViewModel
import java.net.UnknownHostException

class MovieDetailsFragment : Fragment(R.layout.layout_movie_details) {
    companion object {
        const val extraMovie = "extraMovie"
    }

    private var title: MaterialTextView? = null
    private var voteCount: MaterialTextView? = null
    private var voteAverage: AppCompatRatingBar? = null
    private var overview: MaterialTextView? = null
    private var tagLine: MaterialTextView? = null
    private var releaseDate: MaterialTextView? = null
    private var budget: MaterialTextView? = null
    private var homepage: MaterialButton? = null
    private var poster: ShapeableImageView? = null

    private var disposables = CompositeDisposable()
    private var isFavorite: Boolean? = null
    private var movieDetails: MovieDetails? = null

    private val viewModel by activityViewModels<MoviesFavoriteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        title = view.findViewById(R.id.title)
        voteCount = view.findViewById(R.id.voteCount)
        voteAverage = view.findViewById(R.id.voteAverage)
        overview = view.findViewById(R.id.overview)
        tagLine = view.findViewById(R.id.tagLine)
        releaseDate = view.findViewById(R.id.releaseDate)
        budget = view.findViewById(R.id.budget)
        homepage = view.findViewById(R.id.homepage)
        poster = view.findViewById(R.id.poster)

        tagLine?.visibility = View.GONE
        releaseDate?.visibility = View.GONE
        budget?.visibility = View.GONE
        homepage?.visibility = View.GONE

        val movie = arguments?.getParcelable<Parcelable>(extraMovie)
        if (movie is MovieDetails) {
            populateBody(movie)
            return
        }

        if (movie is Movie) {
            populateHead(movie)
            requestDataSource(movie)
            setupFavorite(movie.id)
            return
        }

        showMessage(getString(R.string.an_error_has_occurred))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        isFavorite?.let {
            inflater.inflate(R.menu.movie_menu, menu)

            if (it) {
                val item = menu.findItem(R.id.favorite)
                item?.setIcon(R.drawable.icon_favorite_yellow)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                changeFavorite(requireContext())
                return true
            }
        }

        return false
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    private fun requestDataSource(movie: Movie) {
        val disposable = RetrofitManager()
            .getWebAPI()
            .requestMovieDetails(movie.id, WebConstants.apiKey)
            .compose(RxManager.singleTransformer())
            .subscribe(::populateBody, ::onError)

        disposables.add(disposable)
    }

    private fun populateHead(movie: Movie) {
        requireActivity().title = movie.title

        title?.text = movie.title
        voteCount?.text = getString(R.string.votes, movie.voteCount.toString())
        voteAverage?.rating = movie.voteAverage
        overview?.text = movie.overview
    }

    private fun populateBody(movieDetails: MovieDetails) {
        this.movieDetails = movieDetails

        setupFavorite(movieDetails.id)

        populateHead(movieDetails)

        if (movieDetails.tagLine.isNotEmpty()) {
            tagLine?.apply {
                visibility = View.VISIBLE
                text = movieDetails.tagLine
            }
        }

        if (movieDetails.releaseDate.isNotEmpty()) {
            releaseDate?.apply {
                visibility = View.VISIBLE
                text = getString(R.string.release_date, movieDetails.releaseDate)
            }
        }

        budget?.apply {
            visibility = View.VISIBLE
            text = getString(R.string.budget, movieDetails.budget.toString())
        }

        if (!movieDetails.homepage.isNullOrEmpty()) {
            homepage?.apply {
                visibility = View.VISIBLE

                setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(movieDetails.homepage)
                    startActivity(intent)
                }
            }
        }

        val image = poster ?: return

        val requestOptions = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        val url = WebConstants.imageUrl + movieDetails.posterPath

        Glide.with(this)
            .load(url)
            .apply(requestOptions)
            .into(image)
    }

    private fun onError(error: Throwable) {
        if (error is UnknownHostException) {
            showMessage(getString(R.string.probably_no_connection))
        } else {
            val message = error.message ?: error.toString()
            showMessage(message)
        }
    }

    private fun showMessage(message: String) {
        val view = view ?: return
        Snackbar.make(view.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setupFavorite(id: Int) {
        val context = context ?: return

        fun start(id: Int): Wrap<MovieDetails> {
            val favoriteMovies = Preference.getFavoriteMovies(context)
            val movieDetails = favoriteMovies.firstOrNull { it.id == id }
            return Wrap(movieDetails)
        }

        fun onSuccess(wrap: Wrap<MovieDetails>) {
            if (this.movieDetails == null) {
                this.movieDetails = wrap.data
            }

            if (this.movieDetails != null) {
                updateOptionsMenu(wrap.data != null)
            }
        }

        val disposable = Single.fromCallable { start(id) }
            .compose(RxManager.singleTransformer())
            .subscribe(::onSuccess, ::justError)

        disposables.add(disposable)
    }

    private fun changeFavorite(context: Context) {
        val movieDetails = movieDetails
        if (movieDetails == null) {
            showMessage(getString(R.string.movie_page_has_not_loaded_yet))
            return
        }

        viewModel.isReloadDataSourceRequired = true

        fun start(movieDetails: MovieDetails): Boolean {
            val favoriteMovies = Preference.getFavoriteMovies(context)
            val result = favoriteMovies.firstOrNull { it.id == movieDetails.id }

            return if (result == null) {
                favoriteMovies.add(movieDetails)
                Preference.setFavoriteMovies(context, favoriteMovies)
                true
            } else {
                favoriteMovies.remove(result)
                Preference.setFavoriteMovies(context, favoriteMovies)
                false
            }
        }

        val disposable = Single.fromCallable { start(movieDetails) }
            .compose(RxManager.singleTransformer())
            .subscribe(::updateOptionsMenu, ::justError)

        disposables.add(disposable)
    }

    private fun updateOptionsMenu(isFavorite: Boolean) {
        this.isFavorite = isFavorite
        requireActivity().invalidateOptionsMenu()
    }

    private fun justError(error: Throwable) {
        error.printStackTrace()
    }
}