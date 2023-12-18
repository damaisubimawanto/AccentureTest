package com.damai.domain.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.damai.domain.entities.UserFavoriteEntity

/**
 * Created by damai007 on 18/December/2023
 */
@Dao
interface UserFavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserFavoriteEntity)

    @Query("SELECT * FROM user_favorites WHERE id LIKE :userId")
    fun getUserFavorite(userId: Int): UserFavoriteEntity?

    @Query("SELECT * FROM user_favorites")
    fun getAllUserFavorites(): List<UserFavoriteEntity>

    @Query("DELETE FROM user_favorites WHERE id LIKE :userId")
    fun removeUserFavorite(userId: Int)
}