package com.damai.accenturetest.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.damai.domain.daos.RemoteKeyDao
import com.damai.domain.daos.UserDao
import com.damai.domain.entities.RemoteKeyEntity
import com.damai.domain.entities.UserEntity

/**
 * Created by damai007 on 17/July/2023
 */
@Database(
    entities = [
        UserEntity::class,
        RemoteKeyEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object {
        fun buildDatabase(application: Application): AppDatabase {
            return Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                "accenture-main-database"
            ).fallbackToDestructiveMigration().build()
        }
    }
}