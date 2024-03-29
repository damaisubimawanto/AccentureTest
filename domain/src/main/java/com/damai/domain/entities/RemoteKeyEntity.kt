package com.damai.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by damai007 on 16/December/2023
 */
@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val userId: Int,
    val nextKey: Int?
)
