package com.damai.accenturetest.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.room.withTransaction
import com.damai.accenturetest.room.AppDatabase
import com.damai.base.coroutines.DispatcherProvider
import com.damai.base.extensions.asLiveData
import com.damai.base.utils.Constants.INITIAL_LOAD_SIZE
import com.damai.base.utils.Constants.PER_PAGE_SIZE
import com.damai.base.utils.Constants.PREFETCH_DISTANCE
import com.damai.base.utils.Constants.TAG_REMOTE_MEDIATOR
import com.damai.base.utils.Event
import com.damai.base.utils.UserSharedPreferencesHelper
import com.damai.data.apiservices.MainService
import com.damai.data.mappers.UserDetailsResponseToRemoteKeyEntityMapper
import com.damai.data.mappers.UserDetailsResponseToUserEntityMapper
import com.damai.data.mappers.UserEntityToUserDetailsModelMapper
import com.damai.data.mappers.UserFavoriteEntityToUserDetailsModelMapper
import com.damai.data.repos.RemoteMediatorInterface
import com.damai.data.repos.UserDetailsListRemoteMediator
import com.damai.domain.models.UserDetailsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by damai007 on 16/December/2023
 */
@Singleton
class MainViewModel @Inject constructor(
    /*private val userListPaging: UserDetailsListPagingSource,*/
    private val mainService: MainService,
    private val database: AppDatabase,
    private val dispatcherProvider: DispatcherProvider,
    private val userSharedPreferencesHelper: UserSharedPreferencesHelper,
    private val userDetailsToUserEntityMapper: UserDetailsResponseToUserEntityMapper,
    private val userDetailsToRemoteKeyEntityMapper: UserDetailsResponseToRemoteKeyEntityMapper,
    private val userEntityToUserDetailsModelMapper: UserEntityToUserDetailsModelMapper,
    private val userFavoriteEntityToUserDetailsModelMapper: UserFavoriteEntityToUserDetailsModelMapper
) : ViewModel() {

    //region Live Data
    private val _userClickedLiveData = MutableLiveData<Event<Pair<Int, String>>>()
    val userClickedLiveData = _userClickedLiveData.asLiveData()

    private val _userSearchLiveData = MutableLiveData<Event<String>>()
    val userSearchLiveData = _userSearchLiveData.asLiveData()

    private val _userFavoriteListLiveData = MutableLiveData<List<UserDetailsModel>>()
    val userFavoriteListLiveData = _userFavoriteListLiveData.asLiveData()
    //endregion `Live Data`

    //region Variables
    private val userFavoriteDao = database.userFavoriteDao()
    var mQueryText = ""
    //endregion `Variables`

    //region Public Functions
    fun triggerUserClick(
        userId: Int,
        username: String
    ) {
        Event(
            Pair(userId, username)
        ).let(_userClickedLiveData::setValue)
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
                initialLoadSize = INITIAL_LOAD_SIZE,
                pageSize = PER_PAGE_SIZE,
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
            Log.d(TAG_REMOTE_MEDIATOR, "MainViewModel -> getUserList() -> pagingSourceFactory")
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

    fun getUserFavoriteList() {
        viewModelScope.launch(dispatcherProvider.io()) {
            database.withTransaction {
                userFavoriteDao.getAllUserFavorites().let { userFavoriteEntities ->
                    userFavoriteEntityToUserDetailsModelMapper.map(userFavoriteEntities).let(
                        _userFavoriteListLiveData::postValue
                    )
                }
            }
        }
    }
    //endregion `Public Functions`
}