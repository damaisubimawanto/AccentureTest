package com.damai.data.repos

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.damai.base.extensions.nextLinkKey
import com.damai.base.utils.Constants.HEADER_LINK_NAME
import com.damai.base.utils.Constants.NETWORK_PAGE_SIZE
import com.damai.base.utils.UserSharedPreferencesHelper
import com.damai.data.apiservices.MainService
import com.damai.data.mappers.UserDetailsResponseToRemoteKeyEntityMapper
import com.damai.data.mappers.UserDetailsResponseToUserEntityMapper
import com.damai.domain.daos.RemoteKeyDao
import com.damai.domain.daos.UserDao
import com.damai.domain.entities.UserEntity
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by damai007 on 17/July/2023
 */
@OptIn(ExperimentalPagingApi::class)
class UserDetailsListRemoteMediator(
    private val mainService: MainService,
    private val database: RoomDatabase,
    private val userDao: UserDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val userSharedPreferencesHelper: UserSharedPreferencesHelper,
    private val userDetailsToUserEntityMapper: UserDetailsResponseToUserEntityMapper,
    private val userDetailsToRemoteKeyEntityMapper: UserDetailsResponseToRemoteKeyEntityMapper,
    private val remoteInterface: RemoteMediatorInterface
) : RemoteMediator<Int, UserEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    if (remoteInterface.getQuery().isBlank()) {
                        // Get the last page that was retrieved, that contained items.
                        // From that last page, get the last item
                        state.pages.lastOrNull {
                            it.data.isNotEmpty()
                        }?.data?.lastOrNull()?.let { userEntity ->
                            // Get the remote keys of the first items retrieved
                            val remoteKeyEntity = remoteKeyDao.remoteKeyByUserId(
                                userId = userEntity.id
                            )
                            Log.d(TAG, "RemoteMediator -> APPEND -> user entity ID = ${userEntity.id}, remote next key = ${remoteKeyEntity?.nextKey}")
                            if (remoteKeyEntity?.nextKey == null) {
                                return MediatorResult.Success(endOfPaginationReached = true)
                            }
                            remoteKeyEntity.nextKey
                        } ?: run {
                            Log.d(TAG, "RemoteMediator -> APPEND -> Last data is null")
                            return MediatorResult.Success(endOfPaginationReached = true)
                        }
                    } else {
                        val remoteKeyEntity = remoteKeyDao.remoteKeyLatest()
                        Log.d(TAG, "RemoteMediator -> APPEND -> latest remote next key = ${remoteKeyEntity.nextKey}")
                        if (remoteKeyEntity.nextKey == null) {
                            return MediatorResult.Success(endOfPaginationReached = true)
                        }
                        remoteKeyEntity.nextKey
                    }
                }
            }

            val response = mainService.getUserListAsync(
                since = loadKey,
                perPage = NETWORK_PAGE_SIZE
            ).await()

            /* Get the next key from response headers. */
            val headers = response.headers()
            val link = headers[HEADER_LINK_NAME]
            val nextKey = link.nextLinkKey()
            Log.d(TAG, "UserDetailsListRemoteMediator -> load() -> link header = $link, next key = $nextKey, load type = $loadType")

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearAll()
                    userDao.clearAll()
                }

                response.body()?.let { body ->
                    userDetailsToRemoteKeyEntityMapper.setNextKey(nextKey = nextKey)
                        .map(body)
                        .let { remoteKeys ->
                            remoteKeyDao.insertAll(remoteKeys = remoteKeys)
                        }

                    userDetailsToUserEntityMapper.map(body).let { users ->
                        userDao.insertAll(users = users)
                    }
                }
            }
            /*val isEndPagination = nextKey == null ||
                    response.body().isNullOrEmpty() ||
                    response.body()*/
            Log.d(TAG, "MediatorResult.Success => end of pagination reached = ${nextKey == null}")
            MediatorResult.Success(endOfPaginationReached = nextKey == null)
        } catch (e: IOException) {
            Log.e(TAG, "UserDetailsListRemoteMediator => ERROR!", e)
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.e(TAG, "UserDetailsListRemoteMediator => ERROR!", e)
            MediatorResult.Error(e)
        } catch (e: RuntimeException) {
            Log.e(TAG, "UserDetailsListRemoteMediator => ERROR!", e)
            MediatorResult.Error(e)
        } catch (e: Exception) {
            Log.e(TAG, "UserDetailsListRemoteMediator => ERROR!", e)
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        return if (System.currentTimeMillis() - userSharedPreferencesHelper.userListLastTimeUpdated <= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    companion object {
        private const val TAG = "UserListRemote"
    }
}

interface RemoteMediatorInterface {
    fun getQuery(): String
}