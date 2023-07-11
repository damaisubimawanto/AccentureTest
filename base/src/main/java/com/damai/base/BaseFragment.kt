package com.damai.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

/**
 * Created by damai007 on 10/July/2023
 */
abstract class BaseFragment<VB: ViewBinding, VM: ViewModel> : Fragment() {

    //region Abstract implementation
    abstract fun getViewBinding(): VB

    abstract val viewModel: VM
    //endregion `Abstract implementation`

    private var _binding: VB? = null
    protected val binding
        get() = requireNotNull(_binding)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupDaggerInjection()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewInitialization()
        binding.setupListeners()
        binding.setupObservers()
        binding.onPreparationFinished()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //region Optional implementation
    open fun setupDaggerInjection() {}

    open fun VB.viewInitialization() {}

    open fun VB.setupListeners() {}

    open fun VB.setupObservers() {}

    open fun VB.onPreparationFinished() {}
    //endregion `Optional implementation`
}