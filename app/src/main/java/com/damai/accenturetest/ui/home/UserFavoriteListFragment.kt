package com.damai.accenturetest.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.damai.accenturetest.application.MyApplication
import com.damai.accenturetest.databinding.FragmentUserFavoriteListBinding
import com.damai.accenturetest.ui.MainViewModel
import com.damai.accenturetest.ui.home.adapter.UserFavoriteListAdapter
import com.damai.accenturetest.ui.home.adapter.UserListAdapter
import com.damai.base.BaseFragment
import com.damai.base.extensions.observe
import javax.inject.Inject

/**
 * Created by damai007 on 16/December/2023
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
            mUserFavoriteAdapter = UserFavoriteListAdapter { userId, username ->
                if (userId > 0 && username.isBlank().not()) {
                    viewModel.triggerUserClick(
                        userId = userId,
                        username = username
                    )
                }
            }
            adapter = mUserFavoriteAdapter
        }
    }

    override fun FragmentUserFavoriteListBinding.setupObservers() {
        observe(viewModel.userFavoriteListLiveData) {
            mUserFavoriteAdapter.submitList(it)
        }
    }

    override fun FragmentUserFavoriteListBinding.onPreparationFinished() {
        viewModel.getUserFavoriteList()
    }
}