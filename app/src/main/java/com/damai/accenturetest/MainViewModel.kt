package com.damai.accenturetest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.damai.base.coroutines.DispatcherProvider
import com.damai.base.networks.Resource
import com.damai.data.repos.UserDetailsListPagingSource
import com.damai.domain.models.UserDetailsModel
import com.damai.domain.usecases.UserDetailsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by damai007 on 10/July/2023
 */
class MainViewModel @Inject constructor(
    private val userDetailsUseCase: UserDetailsUseCase,
    private val dispatcherProvider: DispatcherProvider,
    private val userListPaging: UserDetailsListPagingSource
) : ViewModel() {

    fun getUserList(): Flow<PagingData<UserDetailsModel>> {
        return Pager(
            config = PagingConfig(pageSize = 20, maxSize = 500),
            pagingSourceFactory = { userListPaging }
        ).flow.cachedIn(viewModelScope)
    }

    fun getUserDetails() {
        viewModelScope.launch(dispatcherProvider.io()) {
            userDetailsUseCase("jnewland").collect { resource ->
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