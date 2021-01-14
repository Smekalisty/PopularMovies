package ui.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import ui.main.favorite.MoviesFavoriteFragment
import ui.main.popular.MoviesPopularFragment

class MainAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return MoviesPopularFragment()
        }

        return MoviesFavoriteFragment()
    }
}