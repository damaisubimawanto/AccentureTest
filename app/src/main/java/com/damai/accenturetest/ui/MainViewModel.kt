package com.damai.accenturetest.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.damai.base.extensions.asLiveData
import com.damai.base.utils.Event
import com.damai.data.mappers.UserEntityToUserDetailsModelMapper
import com.damai.data.repos.UserDetailsListPagingSource
import com.damai.data.repos.UserDetailsListRemoteMediator
import com.damai.domain.daos.UserDao
import com.damai.domain.models.UserDetailsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by damai007 on 10/July/2023
 */
@Singleton
class MainViewModel @Inject constructor(
    private val userListPaging: UserDetailsListPagingSource,
    private val userListRemoteMediator: UserDetailsListRemoteMediator,
    private val userDao: UserDao,
    private val userEntityToUserDetailsModelMapper: UserEntityToUserDetailsModelMapper
) : ViewModel() {

    //region Live Data
    private val _userClickedLiveData = MutableLiveData<Event<String>>()
    val userClickedLiveData = _userClickedLiveData.asLiveData()
    //endregion `Live Data`

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

    /**
     * This function is for getting user list by query string.
     *
     * @param queryString   The query of username.
     * @return  A paging data which contains of selected user detail model list.
     */
    @OptIn(ExperimentalPagingApi::class)
    fun getUserList(queryString: String): Flow<PagingData<UserDetailsModel>> {
        return Pager(
            config = PagingConfig(pageSize = 20, maxSize = 500),
            remoteMediator = userListRemoteMediator
        ) { /*pagingSourceFactory*/
            userDao.pagingSource(query = "mojombo")
        }.flow.map { userEntityPagingData ->
            userEntityPagingData.map { userEntity ->
                userEntityToUserDetailsModelMapper.map(userEntity)
            }
        }.cachedIn(viewModelScope)
    }
    //endregion `Public Functions`
}