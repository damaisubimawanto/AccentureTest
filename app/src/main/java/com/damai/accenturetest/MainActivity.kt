package com.damai.accenturetest

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.damai.accenturetest.application.MyApplication
import com.damai.accenturetest.databinding.ActivityMainBinding
import com.damai.accenturetest.ui.adapter.UserListAdapter
import com.damai.base.BaseActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    private lateinit var mUserListAdapter: UserListAdapter

    @Inject
    override lateinit var viewModel: MainViewModel

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun setupDaggerInjection() {
        (applicationContext as MyApplication).appComponent.inject(this)
    }

    override fun ActivityMainBinding.viewInitialization() {
        with(rvUserList) {
            mUserListAdapter = UserListAdapter()
            adapter = mUserListAdapter
        }
    }

    override fun ActivityMainBinding.onPreparationFinished() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUserList().collectLatest {
                    mUserListAdapter.submitData(it)
                }
            }
        }
    }
}