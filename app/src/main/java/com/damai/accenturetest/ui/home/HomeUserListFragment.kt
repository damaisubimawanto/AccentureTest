package com.damai.accenturetest.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.damai.accenturetest.application.MyApplication
import com.damai.accenturetest.databinding.FragmentHomeUserListBinding
import com.damai.accenturetest.ui.MainViewModel
import com.damai.base.BaseFragment
import javax.inject.Inject

/**
 * Created by damai007 on 12/July/2023
 */
class HomeUserListFragment : BaseFragment<FragmentHomeUserListBinding, MainViewModel>() {

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

    }
}