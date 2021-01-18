package ui.main

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.popularmovies.R

class MainFragment : Fragment(R.layout.fragment_main), TabLayoutMediator.TabConfigurationStrategy {

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().setTitle(R.string.application_name)

        val tabs = view.findViewById<TabLayout>(R.id.tabs)
        val viewPager = view.findViewById<ViewPager2>(R.id.pager)
        viewPager.adapter = MainAdapter(this)
        TabLayoutMediator(tabs, viewPager, this).attach()

        if (savedInstanceState == null) {
            postponeEnterTransition()

            view.doOnPreDraw {
                startPostponedEnterTransition()
            }
        }
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        if (position == 0) {
            tab.text = getString(R.string.popular)
        } else {
            tab.text = getString(R.string.favorite)
        }
    }
}