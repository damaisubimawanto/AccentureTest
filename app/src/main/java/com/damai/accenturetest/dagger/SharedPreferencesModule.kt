package com.damai.accenturetest.dagger

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.damai.accenturetest.R
import com.damai.base.utils.UserSharedPreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by damai007 on 16/December/2023
 */
@Module
class SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.run {
            getSharedPreferences(
                getString(R.string.user_shared_preferences_name),
                MODE_PRIVATE
            )
        }
    }

    @Provides
    @Singleton
    fun provideUserSharedPreferencesHelper(
        sharedPreferences: SharedPreferences
    ): UserSharedPreferencesHelper {
        return UserSharedPreferencesHelper(sharedPreferences = sharedPreferences)
    }
}