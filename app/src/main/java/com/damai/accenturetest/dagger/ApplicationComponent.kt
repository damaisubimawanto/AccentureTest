package com.damai.accenturetest.dagger

import android.content.Context
import com.damai.accenturetest.MainActivity
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
        MapperModules::class
    ]
)
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }

    fun inject(mainActivity: MainActivity)
}