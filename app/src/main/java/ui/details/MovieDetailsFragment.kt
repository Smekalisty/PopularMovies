package ui.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.transition.TransitionInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.popularmovies.R
import com.popularmovies.databinding.FragmentMovieDetailsBinding
import constants.Constants
import constants.WebConstants
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import utils.Preference
import ui.tabs.pojo.Movie
import ui.tabs.pojo.MovieDetails
import ui.tabs.favorite.MoviesFavoriteViewModel
import java.net.UnknownHostException

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {
    companion object {
        const val extraMovie = "extraMovie"
    }

    private var isFavorite: Boolean? = null
    private var movieDetails: MovieDetails? = null

    private var binding: FragmentMovieDetailsBinding? = null

    private val favoriteViewModel by activityViewModels<MoviesFavoriteViewModel>()

    private val viewModel by viewModels<MoviesDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentMovieDetailsBinding.bind(view)
        this.binding = binding

        with(binding) {
            tagLine.visibility = View.GONE
            releaseDate.visibility = View.GONE
            budget.visibility = View.GONE
            homepage.visibility = View.GONE
        }

        val movie = arguments?.getParcelable<Parcelable>(extraMovie)
        if (movie is MovieDetails) {
            transition(movie, view)
            populateBody(movie)
            return
        }

        if (movie is Movie) {
            transition(movie, view)
            populateHead(movie)

            lifecycleScope.launchWhenCreated {
                viewModel.flow
                    .onEach(::requestDataSourceDone)
                    .collect()
            }

            viewModel.requestDataSource(movie)

            setupFavorite(movie.id)
            return
        }

        showMessage(getString(R.string.an_error_has_occurred))
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun transition(movie: Movie, view: View) {
        view.transitionName = "${Constants.transitionName}${movie.id}"
        loadPosterLandscape(movie)
    }

    private fun loadPosterLandscape(movie: Movie) {
        val posterLandscape = binding?.posterLandscape ?: return

        val requestOptions = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        val url = WebConstants.imageUrl + movie.backdropPath

        Glide.with(this)
            .load(url)
            .apply(requestOptions)
            .into(posterLandscape)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        isFavorite?.let {
            inflater.inflate(R.menu.movie_menu, menu)

            if (it) {
                val item = menu.findItem(R.id.favorite)
                item?.setIcon(R.drawable.icon_favorite_selected)
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

    private fun requestDataSourceDone(result: Result<MovieDetails>) {
        result.fold(::populateBody, ::onError)
    }

    //TODO parcelize to serialize

    private fun populateHead(movie: Movie) {
        activity?.title = movie.title

        binding?.let {
            it.title.text = movie.title
            it.voteCount.text = getString(R.string.votes, movie.voteCount.toString())
            it.voteAverage.rating = movie.voteAverage
            it.overview.text = movie.overview
        }
    }

    private fun populateBody(movieDetails: MovieDetails) {
        val binding = binding ?: return

        this.movieDetails = movieDetails

        setupFavorite(movieDetails.id)

        loadPosterLandscape(movieDetails)

        populateHead(movieDetails)

        if (movieDetails.tagLine.isNotEmpty()) {
            binding.tagLine.visibility = View.VISIBLE
            binding.tagLine.text = movieDetails.tagLine
        }

        if (movieDetails.releaseDate.isNotEmpty()) {
            binding.releaseDate.visibility = View.VISIBLE
            binding.releaseDate.text = getString(R.string.release_date, movieDetails.releaseDate)
        }

        binding.budget.visibility = View.VISIBLE
        binding.budget.text = getString(R.string.budget, movieDetails.budget.toString())

        if (!movieDetails.homepage.isNullOrEmpty()) {
            binding.homepage.visibility = View.VISIBLE

            binding.homepage.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(movieDetails.homepage)
                startActivity(intent)
            }
        }

        val requestOptions = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        val url = WebConstants.imageUrl + movieDetails.posterPath

        Glide.with(this)
            .load(url)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.posterPortrait)

        //TODO on error - posterPortrait should be invisible
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
        val view = binding?.root ?: return
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setupFavorite(id: Int) {
        lifecycleScope.launch {
            val favoriteMovie = withContext(Dispatchers.IO) {
                try {
                    val favoriteMovies = Preference.getFavoriteMovies(requireContext())
                    favoriteMovies.firstOrNull { it.id == id }
                } catch (e: Exception) {
                    null
                }
            }

            if (movieDetails == null) {
                movieDetails = favoriteMovie
            }

            if (movieDetails != null) {
                setupOptionsMenu(favoriteMovie != null)
            }
        }
    }

    private fun changeFavorite(context: Context) {
        val movieDetails = movieDetails
        if (movieDetails == null) {
            showMessage(getString(R.string.movie_page_has_not_loaded_yet))
            return
        }

        lifecycleScope.launch {
            val result: Pair<Int, Boolean> = withContext(Dispatchers.IO) {
                val favoriteMovies = Preference.getFavoriteMovies(context)
                val result = favoriteMovies.firstOrNull { it.id == movieDetails.id }

                val favoritesSize = if (result == null) {
                    favoriteMovies.add(movieDetails)
                    Preference.setFavoriteMovies(context, favoriteMovies)
                    true
                } else {
                    favoriteMovies.remove(result)
                    Preference.setFavoriteMovies(context, favoriteMovies)
                    false
                }

                favoriteMovies.size to favoritesSize
            }

            favoriteViewModel.isReloadDataSourceRequired = true
            favoriteViewModel.favoritesSize = result.first

            setupOptionsMenu(result.second)
        }
    }

    private fun setupOptionsMenu(isFavorite: Boolean) {
        this.isFavorite = isFavorite
        requireActivity().invalidateOptionsMenu()
    }
}