package com.twixt.pranav.pos.View.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.twixt.pranav.pos.View.Fragment.Tab.HomeTab
import com.twixt.pranav.pos.View.Fragment.Tab.HomeTabFavorites


/**
 * Created by Pranav on 11/24/2017.
 */
class AdapterHomeViewPAger(fm: FragmentManager, internal var count: Int) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            val tab = HomeTab()
            return tab
        }else
        {
            val tab2 = HomeTabFavorites()
            return tab2
        }
    }

    override fun getCount(): Int {
        return count
    }
}