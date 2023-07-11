package com.damai.accenturetest.dagger

import com.damai.base.coroutines.DispatcherProvider
import com.damai.base.coroutines.DispatcherProviderImpl
import com.damai.data.repos.MainRepositoryImpl
import com.damai.domain.repositories.MainRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created by damai007 on 11/July/2023
 */

@Module
abstract class DispatcherProviderModule {

    @Singleton
    @Binds
    abstract fun bindDispatcherProvider(
        dispatcherProvider: DispatcherProviderImpl
    ) : DispatcherProvider
}

@Module
abstract class MainRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMainRepository(
        mainRepository: MainRepositoryImpl
    ) : MainRepository
}