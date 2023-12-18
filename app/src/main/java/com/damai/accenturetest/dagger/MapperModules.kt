package com.damai.accenturetest.dagger

import com.damai.data.mappers.UserDetailsModelToUserFavoriteEntityMapper
import com.damai.data.mappers.UserDetailsResponseToRemoteKeyEntityMapper
import com.damai.data.mappers.UserDetailsResponseToUserDetailsModelMapper
import com.damai.data.mappers.UserDetailsResponseToUserEntityMapper
import com.damai.data.mappers.UserEntityToUserDetailsModelMapper
import com.damai.data.mappers.UserFavoriteEntityToUserDetailsModelMapper
import dagger.Module
import dagger.Provides

/**
 * Created by damai007 on 16/December/2023
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

    @Provides
    fun provideUserFavoriteEntityToUserDetailsModelMapper(): UserFavoriteEntityToUserDetailsModelMapper {
        return UserFavoriteEntityToUserDetailsModelMapper()
    }

    @Provides
    fun provideUserDetailsModelToUserFavoriteEntityMapper(): UserDetailsModelToUserFavoriteEntityMapper {
        return UserDetailsModelToUserFavoriteEntityMapper()
    }
}