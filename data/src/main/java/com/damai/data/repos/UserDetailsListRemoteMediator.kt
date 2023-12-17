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
import com.damai.base.utils.Constants.PER_PAGE_SIZE
import com.damai.base.utils.Constants.TAG_REMOTE_MEDIATOR
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
 * Created by damai007 on 16/December/2023
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
        Log.d(TAG_REMOTE_MEDIATOR, "Remote Mediator -> start load() -> load type = $loadType")
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
                            val remoteKeyEntity = database.withTransaction {
                                remoteKeyDao.remoteKeyByUserId(userId = userEntity.id)
                            }
                            Log.d(TAG_REMOTE_MEDIATOR, "RemoteMediator -> loadKey -> APPEND -> user entity ID = ${userEntity.id}, remote next key = ${remoteKeyEntity?.nextKey}")
                            if (remoteKeyEntity?.nextKey == null) {
                                return MediatorResult.Success(endOfPaginationReached = true)
                            }
                            remoteKeyEntity.nextKey
                        } ?: run {
                            Log.d(TAG_REMOTE_MEDIATOR, "RemoteMediator -> loadKey -> APPEND -> Last data is null bro! -> pages size = ${state.pages.size}")
                            return MediatorResult.Success(endOfPaginationReached = true)
                        }
                    } else {
                        val remoteKeyEntity = database.withTransaction {
                            remoteKeyDao.remoteKeyLatest()
                        }
                        Log.d(TAG_REMOTE_MEDIATOR, "RemoteMediator -> loadKey -> APPEND -> query = ${remoteInterface.getQuery()}, latest remote next key = ${remoteKeyEntity.nextKey}")
                        if (remoteKeyEntity.nextKey == null) {
                            return MediatorResult.Success(endOfPaginationReached = true)
                        }
                        remoteKeyEntity.nextKey
                    }
                }
            }

            Log.d(TAG_REMOTE_MEDIATOR, "Remote Mediator -> getUserListAsync() API -> load key = $loadKey, load type = $loadType")
            val response = mainService.getUserListAsync(
                since = loadKey,
                perPage = PER_PAGE_SIZE
            ).await()

            /* Get the next key from response headers. */
            val headers = response.headers()
            val link = headers[HEADER_LINK_NAME]
            val nextKey = link.nextLinkKey()
            Log.d(TAG_REMOTE_MEDIATOR, "Remote Mediator -> API response -> next key = $nextKey\n-> link header = $link ")

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearAll()
                    userDao.clearAll()
                    userSharedPreferencesHelper.userListLastTimeUpdated = System.currentTimeMillis()
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
            Log.d(TAG_REMOTE_MEDIATOR, "MediatorResult.Success => end of pagination reached = ${nextKey == null}")
            MediatorResult.Success(endOfPaginationReached = nextKey == null)
        } catch (e: IOException) {
            Log.e(TAG_REMOTE_MEDIATOR, "UserDetailsListRemoteMediator => IOException ERROR!", e)
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.e(TAG_REMOTE_MEDIATOR, "UserDetailsListRemoteMediator => HttpException ERROR!", e)
            MediatorResult.Error(e)
        } catch (e: RuntimeException) {
            Log.e(TAG_REMOTE_MEDIATOR, "UserDetailsListRemoteMediator => RuntimeException ERROR!", e)
            MediatorResult.Error(e)
        } catch (e: Exception) {
            Log.e(TAG_REMOTE_MEDIATOR, "UserDetailsListRemoteMediator => Exception ERROR!", e)
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        val action = if (System.currentTimeMillis() - userSharedPreferencesHelper.userListLastTimeUpdated <= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
        Log.d(TAG_REMOTE_MEDIATOR, "Remote Mediator -> initialize() -> $action")
        return action
    }
}

interface RemoteMediatorInterface {
    fun getQuery(): String
}