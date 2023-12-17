package com.damai.accenturetest.dagger

import android.app.Application
import androidx.room.RoomDatabase
import com.damai.accenturetest.room.AppDatabase
import com.damai.domain.daos.RemoteKeyDao
import com.damai.domain.daos.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by damai007 on 16/December/2023
 */
@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return AppDatabase.buildDatabase(application = application)
    }

    @Provides
    fun provideRoomDatabase(appDatabase: AppDatabase): RoomDatabase {
        return appDatabase
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideRemoteKeyDao(appDatabase: AppDatabase): RemoteKeyDao {
        return appDatabase.remoteKeyDao()
    }
}