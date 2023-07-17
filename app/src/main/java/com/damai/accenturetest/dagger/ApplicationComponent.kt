package com.damai.accenturetest.dagger

import com.damai.accenturetest.ui.MainActivity
import com.damai.accenturetest.ui.detail.UserDetailsFragment
import com.damai.accenturetest.ui.home.HomeUserListFragment
import com.damai.accenturetest.ui.home.UserFavoriteListFragment
import com.damai.accenturetest.ui.home.UserListFragment
import dagger.Component
import javax.inject.Singleton

/**
 * Created by damai007 on 10/July/2023
 */
@Singleton
@Component(
    modules = [
        AppModules::class,
        NetworkModule::class,
        DispatcherProviderModule::class,
        MainRepositoryModule::class,
        MapperModules::class,
        PagingSourceModules::class,
        RoomModule::class,
        SharedPreferencesModule::class
    ]
)
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        fun appModule(appModules: AppModules): Builder

        fun build(): ApplicationComponent
    }

    fun inject(mainActivity: MainActivity)

    fun inject(fragment: HomeUserListFragment)

    fun inject(fragment: UserListFragment)

    fun inject(fragment: UserFavoriteListFragment)

    fun inject(fragment: UserDetailsFragment)
}