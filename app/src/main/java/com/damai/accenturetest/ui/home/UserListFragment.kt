package com.damai.accenturetest.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.damai.accenturetest.application.MyApplication
import com.damai.accenturetest.databinding.FragmentUserListBinding
import com.damai.accenturetest.ui.MainViewModel
import com.damai.accenturetest.ui.home.adapter.UserListAdapter
import com.damai.base.BaseFragment
import com.damai.base.extensions.observe
import com.damai.base.utils.Constants.TAG_REMOTE_MEDIATOR
import com.damai.base.utils.EventObserver
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by damai007 on 16/December/2023
 */
class UserListFragment : BaseFragment<FragmentUserListBinding, MainViewModel>() {

    private lateinit var mUserListAdapter: UserListAdapter

    @Inject
    override lateinit var viewModel: MainViewModel

    override fun getViewBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentUserListBinding {
        return FragmentUserListBinding.inflate(inflater, viewGroup, false)
    }

    override fun setupDaggerInjection() {
        (requireContext().applicationContext as MyApplication).appComponent.inject(this)
    }

    override fun FragmentUserListBinding.viewInitialization() {
        with(rvUserList) {
            mUserListAdapter = UserListAdapter { username ->
                if (username.isBlank().not()) {
                    viewModel.triggerUserClick(username = username)
                }
            }
            adapter = mUserListAdapter
        }
    }

    override fun FragmentUserListBinding.setupObservers() {
        observe(viewModel.userSearchLiveData, EventObserver { searchQuery ->
            viewModel.mQueryText = searchQuery
            mUserListAdapter.refresh()
        })
    }

    override fun FragmentUserListBinding.onPreparationFinished() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getUserList().collectLatest {
                Log.d(TAG_REMOTE_MEDIATOR, "UserListFragment -> collect latest getUserList()")
                mUserListAdapter.submitData(it)
            }
        }
    }
}