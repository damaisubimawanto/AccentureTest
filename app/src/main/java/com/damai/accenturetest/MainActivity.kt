package com.damai.accenturetest

import com.damai.accenturetest.application.MyApplication
import com.damai.accenturetest.databinding.ActivityMainBinding
import com.damai.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override lateinit var viewModel: MainViewModel

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun setupDaggerInjection() {
        (applicationContext as MyApplication).appComponent.inject(this)
    }
}