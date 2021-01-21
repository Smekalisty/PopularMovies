package ui.root

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.popularmovies.R
import ui.tabs.favorite.MoviesFavoriteViewModel

class RootFragment : Fragment(R.layout.fragment_root), TabLayout.OnTabSelectedListener, TabLayoutMediator.TabConfigurationStrategy {
    private var tabs: TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        super.onCreate(savedInstanceState)
    }

    private val viewModel by activityViewModels<MoviesFavoriteViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().setTitle(R.string.application_name)

        tabs = view.findViewById(R.id.tabs)
        val viewPager = view.findViewById<ViewPager2>(R.id.pager)
        viewPager.adapter = RootAdapter(this)
        TabLayoutMediator(tabs!!, viewPager, this).attach()

        if (savedInstanceState == null) {
            postponeEnterTransition()

            view.doOnPreDraw {
                startPostponedEnterTransition()
            }
        }

        view.doOnPreDraw {
            tabs?.addOnTabSelectedListener(this)
        }
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        if (position == 0) {
            tab.text = getString(R.string.popular)
        } else {
            tab.text = getString(R.string.favorite)

            if (viewModel.isReloadDataSourceRequired && viewModel.favoritesSize > 0) {
                val orCreateBadge = tab.orCreateBadge
                orCreateBadge.number = viewModel.favoritesSize
            }
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab?.position == 1) {
            tab.removeBadge()
            tabs?.removeOnTabSelectedListener(this)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        if (tab?.position == 1) {
            tab.removeBadge()
            tabs?.removeOnTabSelectedListener(this)
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) { }
}