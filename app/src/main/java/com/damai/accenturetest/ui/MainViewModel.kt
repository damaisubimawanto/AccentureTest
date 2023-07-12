package com.damai.accenturetest.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.damai.base.coroutines.DispatcherProvider
import com.damai.base.extensions.asLiveData
import com.damai.base.networks.Resource
import com.damai.base.utils.Event
import com.damai.data.repos.UserDetailsListPagingSource
import com.damai.domain.models.UserDetailsModel
import com.damai.domain.usecases.UserDetailsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by damai007 on 10/July/2023
 */
@Singleton
class MainViewModel @Inject constructor(
    private val userDetailsUseCase: UserDetailsUseCase,
    private val dispatcherProvider: DispatcherProvider,
    private val userListPaging: UserDetailsListPagingSource
) : ViewModel() {

    //region Live Data
    private val _userClickedLiveData = MutableLiveData<Event<String>>()
    val userClickedLiveData = _userClickedLiveData.asLiveData()

    private val _userDetailsLiveData = MutableLiveData<Event<UserDetailsModel>>()
    val userDetailsLiveData = _userDetailsLiveData.asLiveData()

    private val _userDetailsErrorLiveData = MutableLiveData<Event<String>>()
    val userDetailsErrorLiveData = _userDetailsErrorLiveData.asLiveData()
    //endregion `Live Data`

    //region Variables
    var clickedUsername: String? = null
    //endregion `Variables`

    //region Public Functions
    fun triggerUserClick(username: String) {
        Event(username).let(_userClickedLiveData::setValue)
    }

    fun getUserList(): Flow<PagingData<UserDetailsModel>> {
        return Pager(
            config = PagingConfig(pageSize = 20, maxSize = 500),
            pagingSourceFactory = { userListPaging }
        ).flow.cachedIn(viewModelScope)
    }

    fun getUserDetails(username: String) {
        viewModelScope.launch(dispatcherProvider.io()) {
            userDetailsUseCase(username).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.model?.let { dataModel ->
                            Event(dataModel).let(_userDetailsLiveData::postValue)
                        } ?: run {
                            Event("Duh! Empty nih boss!").let(
                                _userDetailsErrorLiveData::postValue
                            )
                        }
                    }
                    is Resource.Error -> {
                        resource.errorMessage?.let { _errorMessage ->
                            Event(_errorMessage).let(_userDetailsErrorLiveData::postValue)
                        }
                    }
                }
            }
        }
    }
    //endregion `Public Functions`
}