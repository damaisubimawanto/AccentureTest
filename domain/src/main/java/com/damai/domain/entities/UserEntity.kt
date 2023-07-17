package com.damai.domain.entities

import androidx.room.Entity

/**
 * Created by damai007 on 17/July/2023
 */
@Entity(tableName = "users")
data class UserEntity(
    val username: String?,
    val id: Int,
    val avatarUrl: String?,
    val url: String?,
    val name: String?,
    val company: String?,
    val blog: String?,
    val location: String?,
    val email: String?,
    val bio: String?,
    val twitterUsername: String?,
    val publicRepos: Int,
    val publicGists: Int,
    val followers: Int,
    val following: Int,
    val createdAt: String,
    val updatedAt: String
)
