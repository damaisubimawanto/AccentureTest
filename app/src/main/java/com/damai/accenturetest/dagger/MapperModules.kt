package com.damai.accenturetest.dagger

import com.damai.data.mappers.UserDetailsResponseToRemoteKeyEntityMapper
import com.damai.data.mappers.UserDetailsResponseToUserDetailsModelMapper
import com.damai.data.mappers.UserDetailsResponseToUserEntityMapper
import com.damai.data.mappers.UserEntityToUserDetailsModelMapper
import dagger.Module
import dagger.Provides

/**
 * Created by damai007 on 11/July/2023
 */
@Module
class MapperModules {

    @Provides
    fun provideUserDetailsResponseToUserDetailsModelMapper(): UserDetailsResponseToUserDetailsModelMapper {
        return UserDetailsResponseToUserDetailsModelMapper()
    }

    @Provides
    fun provideUserDetailsResponseToUserEntityMapper(): UserDetailsResponseToUserEntityMapper {
        return UserDetailsResponseToUserEntityMapper()
    }

    @Provides
    fun provideUserEntityToUserDetailsModelMapper(): UserEntityToUserDetailsModelMapper {
        return UserEntityToUserDetailsModelMapper()
    }

    @Provides
    fun provideUserDetailsResponseToRemoteKeyEntityMapper(): UserDetailsResponseToRemoteKeyEntityMapper {
        return UserDetailsResponseToRemoteKeyEntityMapper()
    }
}