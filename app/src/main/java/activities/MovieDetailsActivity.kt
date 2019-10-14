package activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
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
import java.net.UnknownHostException

class MovieDetailsActivity : AppCompatActivity() {
    companion object {
        const val extraMovie = "extraMovie"
    }

    private var title: AppCompatTextView? = null
    private var voteCount: AppCompatTextView? = null
    private var voteAverage: AppCompatRatingBar? = null
    private var overview: AppCompatTextView? = null
    private var tagLine: AppCompatTextView? = null
    private var releaseDate: AppCompatTextView? = null
    private var budget: AppCompatTextView? = null
    private var homepage: AppCompatButton? = null
    private var poster: AppCompatImageView? = null

    private var disposables = CompositeDisposable()
    private var isFavorite: Boolean? = null
    private var movieDetails: MovieDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        title = findViewById(R.id.title)
        voteCount = findViewById(R.id.voteCount)
        voteAverage = findViewById(R.id.voteAverage)
        overview = findViewById(R.id.overview)
        tagLine = findViewById(R.id.tagLine)
        releaseDate = findViewById(R.id.releaseDate)
        budget = findViewById(R.id.budget)
        homepage = findViewById(R.id.homepage)
        poster = findViewById(R.id.poster)

        tagLine?.visibility = View.GONE
        releaseDate?.visibility = View.GONE
        budget?.visibility = View.GONE
        homepage?.visibility = View.GONE

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val movie = intent.getParcelableExtra<Parcelable>(extraMovie)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        isFavorite?.let {
            menuInflater.inflate(R.menu.movie_menu, menu)

            if (it) {
                val item = menu?.findItem(R.id.favorite)
                item?.setIcon(R.drawable.icon_favorite_yellow)
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.favorite -> {
                changeFavorite()
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
        setTitle(movie.title)

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
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setupFavorite(id: Int) {
        fun start(id: Int): Wrap<MovieDetails> {
            val favoriteMovies = Preference.getFavoriteMovies(this)
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

    private fun changeFavorite() {
        val movieDetails = movieDetails
        if (movieDetails == null) {
            showMessage(getString(R.string.movie_page_has_not_loaded_yet))
            return
        }

        setResult(Activity.RESULT_OK)

        fun start(movieDetails: MovieDetails): Boolean {
            val favoriteMovies = Preference.getFavoriteMovies(this)
            val result = favoriteMovies.firstOrNull { it.id == movieDetails.id }

            return if (result == null) {
                favoriteMovies.add(movieDetails)
                Preference.setFavoriteMovies(this, favoriteMovies)
                true
            } else {
                favoriteMovies.remove(result)
                Preference.setFavoriteMovies(this, favoriteMovies)
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
        invalidateOptionsMenu()
    }

    private fun justError(error: Throwable) {
        error.printStackTrace()
    }
}