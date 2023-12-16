package com.damai.accenturetest.dagger

import com.damai.data.apiservices.MainService
import com.damai.data.mappers.UserDetailsResponseToUserDetailsModelMapper
import com.damai.data.repos.UserDetailsListPagingSource
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
}