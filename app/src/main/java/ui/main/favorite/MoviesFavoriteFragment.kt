package ui.main.favorite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.popularmovies.R
import entities.helpers.Preference
import entities.helpers.RxManager
import entities.pojo.MovieDetails
import io.reactivex.Single
import ui.main.base.MoviesBaseFragment

class MoviesFavoriteFragment : MoviesBaseFragment() {
    private var adapter: Adapter? = null
    private var requestCode = 1

    override fun getTitle() = R.string.favorite_movies

    override fun setAdapter(recyclerView: RecyclerView) {
        adapter = Adapter(getClickSubject())
        recyclerView.adapter = adapter
    }

    override fun requestDataSource() {
        setFragmentResultListener("x", { a, b ->
            val char = b.getChar("x")
            println(char)
        })

        val disposable = Single.fromCallable { Preference.getFavoriteMovies(requireContext()) }
            .compose(RxManager.singleTransformer())
            .subscribe(::onDataSourceLoaded, ::onError)

        disposables.add(disposable)
    }

    override fun showTopLevelFragment(view: View, bundle: Bundle) {
        findNavController().navigate(R.id.master_to_details, bundle)
        //TODO requestCode
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                requestDataSource()
            }
        }
    }

    private fun onDataSourceLoaded(dataSource: MutableList<MovieDetails>) {
        onDataSourceLoaded(dataSource.isNotEmpty())
        adapter?.submitList(dataSource)
    }
}