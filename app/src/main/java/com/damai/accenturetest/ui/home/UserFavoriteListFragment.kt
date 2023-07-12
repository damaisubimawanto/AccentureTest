package com.damai.accenturetest.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.damai.accenturetest.application.MyApplication
import com.damai.accenturetest.databinding.FragmentUserFavoriteListBinding
import com.damai.accenturetest.ui.MainViewModel
import com.damai.accenturetest.ui.home.adapter.UserFavoriteListAdapter
import com.damai.base.BaseFragment
import javax.inject.Inject

/**
 * Created by damai007 on 12/July/2023
 */
class UserFavoriteListFragment : BaseFragment<FragmentUserFavoriteListBinding, MainViewModel>() {

    private lateinit var mUserFavoriteAdapter: UserFavoriteListAdapter

    @Inject
    override lateinit var viewModel: MainViewModel

    override fun getViewBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentUserFavoriteListBinding {
        return FragmentUserFavoriteListBinding.inflate(inflater, viewGroup, false)
    }

    override fun setupDaggerInjection() {
        (requireContext().applicationContext as MyApplication).appComponent.inject(this)
    }

    override fun FragmentUserFavoriteListBinding.viewInitialization() {
        with(rvUserList) {
            mUserFavoriteAdapter = UserFavoriteListAdapter()
            adapter = mUserFavoriteAdapter
        }
    }
}