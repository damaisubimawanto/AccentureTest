package com.damai.accenturetest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.damai.base.coroutines.DispatcherProvider
import com.damai.base.networks.Resource
import com.damai.domain.usecases.UserDetailsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by damai007 on 10/July/2023
 */
class MainViewModel @Inject constructor(
    private val userDetailsUseCase: UserDetailsUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    fun getUserList() {

    }

    fun getUserDetails() {
        viewModelScope.launch(dispatcherProvider.io()) {
            userDetailsUseCase().collect { resource ->
                when (resource) {
                    is Resource.Success -> {

                    }
                    is Resource.Error -> {

                    }
                }
            }
        }
    }
}