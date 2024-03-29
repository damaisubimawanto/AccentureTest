package com.damai.accenturetest.ui

import com.damai.accenturetest.application.MyApplication
import com.damai.accenturetest.databinding.ActivityMainBinding
import com.damai.base.BaseActivity
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    @Inject
    override lateinit var viewModel: MainViewModel

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun setupDaggerInjection() {
        (applicationContext as MyApplication).appComponent.inject(this)
    }
}