package com.damai.accenturetest.dagger

import android.content.Context
import com.damai.accenturetest.ui.MainActivity
import com.damai.accenturetest.ui.home.HomeUserListFragment
import com.damai.accenturetest.ui.home.UserListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Created by damai007 on 10/July/2023
 */
@Singleton
@Component(
    modules = [
        NetworkModule::class,
        DispatcherProviderModule::class,
        MainRepositoryModule::class,
        MapperModules::class,
        PagingSourceModules::class
    ]
)
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }

    fun inject(mainActivity: MainActivity)

    fun inject(fragment: HomeUserListFragment)

    fun inject(fragment: UserListFragment)
}