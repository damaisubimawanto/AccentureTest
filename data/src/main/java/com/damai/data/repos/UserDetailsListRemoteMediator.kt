package com.damai.data.repos

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.damai.base.extensions.nextLinkKey
import com.damai.base.utils.Constants.HEADER_LINK_NAME
import com.damai.base.utils.UserSharedPreferencesHelper
import com.damai.data.apiservices.MainService
import com.damai.data.mappers.UserDetailsResponseToUserEntityMapper
import com.damai.domain.daos.RemoteKeyDao
import com.damai.domain.daos.UserDao
import com.damai.domain.entities.RemoteKeyEntity
import com.damai.domain.entities.UserEntity
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by damai007 on 17/July/2023
 */
@OptIn(ExperimentalPagingApi::class)
class UserDetailsListRemoteMediator @Inject constructor(
    private val mainService: MainService,
    private val userDao: UserDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val userSharedPreferencesHelper: UserSharedPreferencesHelper,
    private val userDetailsToUserEntityMapper: UserDetailsResponseToUserEntityMapper
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
                    val remoteKeyEntity = remoteKeyDao.remoteKeyByQuery(query = "")
                    if (remoteKeyEntity.nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    remoteKeyEntity.nextKey
                }
            }

            val response = mainService.getUserListAsync(since = loadKey).await()

            /* Get the next key from response headers. */
            val headers = response.headers()
            val link = headers[HEADER_LINK_NAME]
            val nextKey = link.nextLinkKey()
            Log.d(TAG, "UserDetailsListRemoteMediator -> load() -> link header = $link, next key = $nextKey")

            if (loadType == LoadType.REFRESH) {
                remoteKeyDao.deleteByQuery(query = nextKey?.toString().orEmpty())
                userDao.clearAll()
            }

            response.body()?.let { _body ->
                remoteKeyDao.insertOrReplace(
                    RemoteKeyEntity(
                        label = "",
                        nextKey = nextKey
                    )
                )

                userDao.insertAll(
                    userDetailsToUserEntityMapper.setSince(since = nextKey).map(_body)
                )
            }

            MediatorResult.Success(endOfPaginationReached = nextKey == null)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: RuntimeException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
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