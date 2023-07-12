package com.damai.accenturetest.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.damai.accenturetest.R
import com.damai.accenturetest.application.MyApplication
import com.damai.accenturetest.databinding.FragmentHomeUserListBinding
import com.damai.accenturetest.ui.MainViewModel
import com.damai.accenturetest.ui.home.adapter.HomePagerAdapter
import com.damai.base.BaseFragment
import com.damai.base.extensions.navigateTo
import com.damai.base.extensions.observe
import com.damai.base.utils.Constants.BUNDLE_ARGS_USERNAME
import com.damai.base.utils.EventObserver
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

/**
 * Created by damai007 on 12/July/2023
 */
class HomeUserListFragment : BaseFragment<FragmentHomeUserListBinding, MainViewModel>() {

    private lateinit var mHomePagerAdapter: HomePagerAdapter

    @Inject
    override lateinit var viewModel: MainViewModel

    override fun getViewBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentHomeUserListBinding {
        return FragmentHomeUserListBinding.inflate(inflater, viewGroup, false)
    }

    override fun setupDaggerInjection() {
        (requireContext().applicationContext as MyApplication).appComponent.inject(this)
    }

    override fun FragmentHomeUserListBinding.viewInitialization() {
        with(vpUserList) {
            mHomePagerAdapter = HomePagerAdapter(fragment = this@HomeUserListFragment)
            adapter = mHomePagerAdapter
        }

        with(tabLayout) {
            TabLayoutMediator(this, vpUserList) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.tab_popular)
                    else -> tab.text = getString(R.string.tab_favorite)
                }
            }.attach()
        }
    }

    override fun FragmentHomeUserListBinding.setupObservers() {
        observe(viewModel.userClickedLiveData, EventObserver {
            navigateTo(
                resId = R.id.action_home_user_list_to_user_details,
                bundle = bundleOf(BUNDLE_ARGS_USERNAME to it)
            )
        })
    }
}