package com.damai.accenturetest.dagger

import com.damai.data.mappers.UserDetailsResponseToUserDetailsModelMapper
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
}