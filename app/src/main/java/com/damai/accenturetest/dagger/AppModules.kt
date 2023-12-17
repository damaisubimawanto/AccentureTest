package com.damai.accenturetest.dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by damai007 on 16/December/2023
 */
@Module
class AppModules(
    private val application: Application
) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application.applicationContext
}