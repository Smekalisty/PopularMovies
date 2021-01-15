package ui.main.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.popularmovies.R
import entities.WebConstants
import entities.helpers.Utils
import entities.pojo.Movie
import ui.details.MovieDetailsFragment

class MovieAdapterExecutor {
    fun onCreateViewHolder(viewGroup: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val view = layoutInflater.inflate(R.layout.layout_movie, viewGroup, false)
        return ViewHolder(view)
    }

    fun onBindViewHolder(viewHolder: ViewHolder, movie: Movie) {
        val context = viewHolder.itemView.context

        val placeHolder = Utils.generatePastelDrawable(movie.title)

        val requestOptions = RequestOptions()
            .placeholder(placeHolder)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        val url = WebConstants.imageUrl + movie.backdropPath

        Glide.with(viewHolder.itemView)
            .load(url)
            .apply(requestOptions)
            .into(viewHolder.backdrop)

        viewHolder.title.text = movie.title
        viewHolder.overview.text = movie.overview
        viewHolder.voteCount.text = context.getString(R.string.votes, movie.voteCount.toString())
        viewHolder.voteAverage.rating = movie.voteAverage

        viewHolder.itemView.transitionName = "image_${movie.id}"

        //viewHolder.backdrop.transitionName = "image_${movie.id}"
        //viewHolder.title.transitionName = "title_${movie.id}"

        viewHolder.itemView.setOnClickListener {
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

            val a0 = viewHolder.itemView to "image_${movie.id}"
            //val a1 = viewHolder.backdrop to "image_${movie.id}"
            //val a2 = viewHolder.title to "title_${movie.id}"
            val extras = FragmentNavigatorExtras(a0/*, a1, a2*/)

            it.findNavController().navigate(detailsDirection, extras)
        }
    }
}