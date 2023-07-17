package com.damai.accenturetest.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.damai.base.extensions.asLiveData
import com.damai.base.utils.Event
import com.damai.data.repos.UserDetailsListPagingSource
import com.damai.data.repos.UserDetailsListRemoteMediator
import com.damai.domain.daos.UserDao
import com.damai.domain.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by damai007 on 10/July/2023
 */
@Singleton
class MainViewModel @Inject constructor(
    private val userListPaging: UserDetailsListPagingSource,
    private val userListRemoteMediator: UserDetailsListRemoteMediator,
    private val userDao: UserDao
) : ViewModel() {

    //region Live Data
    private val _userClickedLiveData = MutableLiveData<Event<String>>()
    val userClickedLiveData = _userClickedLiveData.asLiveData()
    //endregion `Live Data`

    //region Public Functions
    fun triggerUserClick(username: String) {
        Event(username).let(_userClickedLiveData::setValue)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getUserList(): Flow<PagingData<UserEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 20, maxSize = 500),
            remoteMediator = userListRemoteMediator
        ) {
            userDao.pagingSource(query = "")
        }.flow.cachedIn(viewModelScope)
    }
    //endregion `Public Functions`
}