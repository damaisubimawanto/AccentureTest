package com.damai.domain.entities

import androidx.room.Entity

/**
 * Created by damai007 on 17/July/2023
 */
@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    val label: String,
    val nextKey: Int?
)
