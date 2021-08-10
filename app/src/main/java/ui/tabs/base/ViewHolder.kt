package ui.tabs.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.popularmovies.R
import constants.Constants
import constants.WebConstants
import database.master.Movie
import ui.details.MovieDetailsFragment
import utils.Utils

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun factory(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.movie_item, parent, false)
            return ViewHolder(view)
        }
    }

    //TODO delete
    private val number: MaterialTextView = view.findViewById(R.id.number)

    private val backdrop: ShapeableImageView = view.findViewById(R.id.backdrop)
    private val title: MaterialTextView = view.findViewById(R.id.title)
    private val overview: MaterialTextView = view.findViewById(R.id.overview)
    private val voteCount: MaterialTextView = view.findViewById(R.id.voteCount)
    private val voteAverage: RatingBar = view.findViewById(R.id.voteAverage)

    fun onBindViewHolder(movie: Movie, position: Int) {
        number.text = position.toString()

        val context = itemView.context

        val placeHolder = Utils.generatePastelDrawable(movie.title)

        val requestOptions = RequestOptions()
            .placeholder(placeHolder)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        val url = WebConstants.imageUrl + movie.backdropPath

        Glide.with(itemView)
            .load(url)
            .timeout(10000)
            .apply(requestOptions)
            .into(backdrop)

        title.text = movie.title
        overview.text = movie.overview
        voteCount.text = context.getString(R.string.votes, movie.voteCount.toString())
        voteAverage.rating = movie.voteAverage

        itemView.transitionName = "${Constants.transitionName}${movie.id}"

        itemView.setOnClickListener {
            val detailsDirection = object : NavDirections {
                override fun getActionId(): Int {
                    return R.id.main_to_details
                }

                override fun getArguments(): Bundle {
                    val bundle = Bundle()
                    bundle.putParcelable(MovieDetailsFragment.extraMovie, movie)
                    return bundle
                }
            }

            val extras = FragmentNavigatorExtras(itemView to "${Constants.transitionName}${movie.id}")

            it.findNavController().navigate(detailsDirection, extras)
        }
    }
}