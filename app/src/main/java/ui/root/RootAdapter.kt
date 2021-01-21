package ui.root

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ui.tabs.favorite.MoviesFavoriteFragment
import ui.tabs.popular.MoviesPopularFragment

class RootAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return MoviesPopularFragment()
        }

        return MoviesFavoriteFragment()
    }
}