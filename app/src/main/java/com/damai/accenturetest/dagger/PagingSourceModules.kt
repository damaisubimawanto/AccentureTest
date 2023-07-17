package com.damai.accenturetest.dagger

import com.damai.base.utils.UserSharedPreferencesHelper
import com.damai.data.apiservices.MainService
import com.damai.data.mappers.UserDetailsResponseToUserDetailsModelMapper
import com.damai.data.mappers.UserDetailsResponseToUserEntityMapper
import com.damai.data.repos.UserDetailsListPagingSource
import com.damai.data.repos.UserDetailsListRemoteMediator
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
        userDao: UserDao,
        userSharedPreferencesHelper: UserSharedPreferencesHelper,
        userDetailsResponseToUserEntityMapper: UserDetailsResponseToUserEntityMapper
    ): UserDetailsListRemoteMediator {
        return UserDetailsListRemoteMediator(
            mainService = mainService,
            userDao = userDao,
            userSharedPreferencesHelper = userSharedPreferencesHelper,
            userDetailsToUserEntityMapper = userDetailsResponseToUserEntityMapper
        )
    }
}