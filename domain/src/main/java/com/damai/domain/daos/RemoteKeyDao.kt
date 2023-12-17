package com.damai.domain.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.damai.domain.entities.RemoteKeyEntity

/**
 * Created by damai007 on 16/December/2023
 */
@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeyEntity>)

    @Query("SELECT * FROM remote_keys WHERE userId = :userId")
    suspend fun remoteKeyByUserId(userId: Int): RemoteKeyEntity?

    @Query("SELECT * FROM remote_keys ORDER BY nextKey DESC LIMIT 1")
    suspend fun remoteKeyLatest(): RemoteKeyEntity

    @Query("DELETE FROM remote_keys")
    suspend fun clearAll()
}