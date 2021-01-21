package ui.tabs.base

import android.view.View
import android.widget.RatingBar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.popularmovies.R

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val backdrop: ShapeableImageView = view.findViewById(R.id.backdrop)
    val title: MaterialTextView = view.findViewById(R.id.title)
    val overview: MaterialTextView = view.findViewById(R.id.overview)
    val voteCount: MaterialTextView = view.findViewById(R.id.voteCount)
    val voteAverage: RatingBar = view.findViewById(R.id.voteAverage)
}