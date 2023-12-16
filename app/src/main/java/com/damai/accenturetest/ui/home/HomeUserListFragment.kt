package com.damai.accenturetest.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import com.damai.accenturetest.R
import com.damai.accenturetest.application.MyApplication
import com.damai.accenturetest.databinding.FragmentHomeUserListBinding
import com.damai.accenturetest.ui.MainViewModel
import com.damai.accenturetest.ui.home.adapter.HomePagerAdapter
import com.damai.base.BaseFragment
import com.damai.base.extensions.addOnTextChanged
import com.damai.base.extensions.gone
import com.damai.base.extensions.hideSoftKeyboard
import com.damai.base.extensions.navigateTo
import com.damai.base.extensions.observe
import com.damai.base.extensions.setCustomOnClickListener
import com.damai.base.extensions.showSoftKeyboard
import com.damai.base.extensions.visible
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

        with(etMainSearch) {
            addOnTextChanged { searchText ->
                if (searchText.isBlank()) {
                    tvMainTitle.visible()
                    etMainSearch.gone()
                    etMainSearch.hideSoftKeyboard()
                    viewModel.getUserList(queryString = "")
                    return@addOnTextChanged
                }
                viewModel.getUserList(queryString = searchText)
            }
        }

        rlSearchButton.setCustomOnClickListener {
            if (etMainSearch.isGone) {
                tvMainTitle.gone()
                etMainSearch.visible()
                etMainSearch.showSoftKeyboard()
            }
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