package ui

import android.view.View
import android.widget.RatingBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.popularmovies.R

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val backdrop: AppCompatImageView = view.findViewById(R.id.backdrop)
    val title: AppCompatTextView = view.findViewById(R.id.title)
    val overview: AppCompatTextView = view.findViewById(R.id.overview)
    val voteCount: AppCompatTextView = view.findViewById(R.id.voteCount)
    val voteAverage: RatingBar = view.findViewById(R.id.voteAverage)
}