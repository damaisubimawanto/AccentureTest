package com.damai.domain.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.damai.domain.entities.UserEntity

/**
 * Created by damai007 on 17/July/2023
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("SELECT * FROM users")
    fun pagingSourceAll(): PagingSource<Int, UserEntity>

    @Query("SELECT * FROM users WHERE username LIKE :query")
    fun pagingSource(query: String): PagingSource<Int, UserEntity>

    @Query("DELETE FROM users")
    suspend fun clearAll()
}