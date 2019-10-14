package ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.popularmovies.R
import entities.WebConstants
import entities.helpers.Utils
import entities.pojo.Movie
import io.reactivex.subjects.PublishSubject

class MovieAdapterExecutor {
    fun onCreateViewHolder(viewGroup: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val view = layoutInflater.inflate(R.layout.layout_movie, viewGroup, false)
        return ViewHolder(view)
    }

    fun onBindViewHolder(viewHolder: ViewHolder, movie: Movie, clickSubject: PublishSubject<Movie>) {
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

        viewHolder.itemView.setOnClickListener {
            clickSubject.onNext(movie)
        }
    }
}