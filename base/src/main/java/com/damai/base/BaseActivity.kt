package com.damai.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

/**
 * Created by damai007 on 16/December/2023
 */
abstract class BaseActivity<VB: ViewBinding, VM: ViewModel> : AppCompatActivity() {

    //region Abstract implementation
    abstract val viewModel: VM

    abstract fun getViewBinding(): VB
    //endregion `Abstract implementation`

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        setupDaggerInjection()
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        binding.viewInitialization()
        binding.setupListeners()
        binding.setupObservers()
        binding.onPreparationFinished()
    }

    //region Optional implementation
    open fun setupDaggerInjection() {}

    open fun VB.viewInitialization() {}

    open fun VB.setupListeners() {}

    open fun VB.setupObservers() {}

    open fun VB.onPreparationFinished() {}
    //endregion `Optional implementation`
}