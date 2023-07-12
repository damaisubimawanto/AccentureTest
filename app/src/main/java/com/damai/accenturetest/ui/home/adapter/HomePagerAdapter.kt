package com.damai.accenturetest.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.damai.accenturetest.ui.home.UserFavoriteListFragment
import com.damai.accenturetest.ui.home.UserListFragment

/**
 * Created by damai007 on 12/July/2023
 */
class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                UserListFragment()
            }
            else -> {
                UserFavoriteListFragment()
            }
        }
    }

    override fun getItemCount(): Int = 2
}