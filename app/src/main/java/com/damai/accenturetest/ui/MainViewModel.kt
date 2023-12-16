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
import com.damai.accenturetest.room.AppDatabase
import com.damai.base.extensions.asLiveData
import com.damai.base.utils.Constants.NETWORK_PAGE_SIZE
import com.damai.base.utils.Constants.PREFETCH_DISTANCE
import com.damai.base.utils.Event
import com.damai.base.utils.UserSharedPreferencesHelper
import com.damai.data.apiservices.MainService
import com.damai.data.mappers.UserDetailsResponseToRemoteKeyEntityMapper
import com.damai.data.mappers.UserDetailsResponseToUserEntityMapper
import com.damai.data.mappers.UserEntityToUserDetailsModelMapper
import com.damai.data.repos.RemoteMediatorInterface
import com.damai.data.repos.UserDetailsListRemoteMediator
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
    /*private val userListPaging: UserDetailsListPagingSource,*/
    private val mainService: MainService,
    private val database: AppDatabase,
    private val userSharedPreferencesHelper: UserSharedPreferencesHelper,
    private val userDetailsToUserEntityMapper: UserDetailsResponseToUserEntityMapper,
    private val userDetailsToRemoteKeyEntityMapper: UserDetailsResponseToRemoteKeyEntityMapper,
    private val userEntityToUserDetailsModelMapper: UserEntityToUserDetailsModelMapper
) : ViewModel() {

    //region Live Data
    private val _userClickedLiveData = MutableLiveData<Event<String>>()
    val userClickedLiveData = _userClickedLiveData.asLiveData()

    private val _userSearchLiveData = MutableLiveData<Event<String>>()
    val userSearchLiveData = _userSearchLiveData.asLiveData()

    var mQueryText = ""
    //endregion `Live Data`

    //region Public Functions
    fun triggerUserClick(username: String) {
        Event(username).let(_userClickedLiveData::setValue)
    }

    fun triggerUserSearch(searchText: String) {
        Event(searchText).let(_userSearchLiveData::postValue)
    }

    /*fun getUserList(): Flow<PagingData<UserDetailsModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            pagingSourceFactory = { userListPaging }
        ).flow.cachedIn(viewModelScope)
    }*/

    /**
     * This function is for getting user list by query string of [mQueryText].
     *
     * @return  A paging data which contains of selected user detail model list.
     */
    @OptIn(ExperimentalPagingApi::class)
    fun getUserList(): Flow<PagingData<UserDetailsModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            remoteMediator = UserDetailsListRemoteMediator(
                mainService = mainService,
                database = database,
                userDao = database.userDao(),
                remoteKeyDao = database.remoteKeyDao(),
                userSharedPreferencesHelper = userSharedPreferencesHelper,
                userDetailsToUserEntityMapper = userDetailsToUserEntityMapper,
                userDetailsToRemoteKeyEntityMapper = userDetailsToRemoteKeyEntityMapper,
                remoteInterface = object : RemoteMediatorInterface {
                    override fun getQuery(): String = mQueryText
                }
            )
        ) { /*pagingSourceFactory*/
            if (mQueryText.isBlank()) {
                database.userDao().pagingSourceAll()
            } else {
                database.userDao().pagingSource(query = mQueryText)
            }
        }.flow.map { userEntityPagingData ->
            userEntityPagingData.map { userEntity ->
                userEntityToUserDetailsModelMapper.map(userEntity)
            }
        }.cachedIn(viewModelScope)
    }
    //endregion `Public Functions`
}