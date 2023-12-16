package com.damai.accenturetest.dagger

import androidx.room.RoomDatabase
import com.damai.accenturetest.room.AppDatabase
import com.damai.base.utils.UserSharedPreferencesHelper
import com.damai.data.apiservices.MainService
import com.damai.data.mappers.UserDetailsResponseToRemoteKeyEntityMapper
import com.damai.data.mappers.UserDetailsResponseToUserDetailsModelMapper
import com.damai.data.mappers.UserDetailsResponseToUserEntityMapper
import com.damai.data.repos.UserDetailsListPagingSource
import com.damai.data.repos.UserDetailsListRemoteMediator
import com.damai.domain.daos.RemoteKeyDao
import com.damai.domain.daos.UserDao
import dagger.Module
import dagger.Provides

/**
 * Created by damai007 on 11/July/2023
 */
@Module
class PagingSourceModules {

    @Provides
    fun provideUserDetailsListPagingSource(
        mainService: MainService,
        userDetailsResponseToUserDetailsModelMapper: UserDetailsResponseToUserDetailsModelMapper
    ): UserDetailsListPagingSource {
        return UserDetailsListPagingSource(
            mainService = mainService,
            userDetailsMapper = userDetailsResponseToUserDetailsModelMapper
        )
    }

    @Provides
    fun provideUserDetailsListRemoteMediator(
        mainService: MainService,
        database: AppDatabase,
        userDao: UserDao,
        remoteKeyDao: RemoteKeyDao,
        userSharedPreferencesHelper: UserSharedPreferencesHelper,
        userDetailsResponseToUserEntityMapper: UserDetailsResponseToUserEntityMapper,
        userDetailsResponseToRemoteKeyEntityMapper: UserDetailsResponseToRemoteKeyEntityMapper
    ): UserDetailsListRemoteMediator {
        return UserDetailsListRemoteMediator(
            mainService = mainService,
            database = database,
            userDao = userDao,
            remoteKeyDao = remoteKeyDao,
            userSharedPreferencesHelper = userSharedPreferencesHelper,
            userDetailsToUserEntityMapper = userDetailsResponseToUserEntityMapper,
            userDetailsToRemoteKeyEntityMapper = userDetailsResponseToRemoteKeyEntityMapper
        )
    }
}