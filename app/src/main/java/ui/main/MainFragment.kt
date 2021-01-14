package ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.popularmovies.R

class MainFragment : Fragment(R.layout.layout_main), TabLayoutMediator.TabConfigurationStrategy {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tabs = view.findViewById<TabLayout>(R.id.tabs)
        val viewPager = view.findViewById<ViewPager2>(R.id.pager)
        viewPager.adapter = MainAdapter(this)
        TabLayoutMediator(tabs, viewPager, this).attach()
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        if (position == 0) {
            tab.text = getString(R.string.popular)
        } else {
            tab.text = getString(R.string.favorite)
        }
    }
}