package com.damai.accenturetest.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.damai.accenturetest.application.MyApplication
import com.damai.accenturetest.databinding.FragmentUserListBinding
import com.damai.accenturetest.ui.MainViewModel
import com.damai.accenturetest.ui.home.adapter.UserListAdapter
import com.damai.base.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by damai007 on 12/July/2023
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
            mUserListAdapter = UserListAdapter()
            adapter = mUserListAdapter
        }
    }

    override fun FragmentUserListBinding.onPreparationFinished() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUserList().collectLatest {
                    mUserListAdapter.submitData(it)
                }
            }
        }
    }
}