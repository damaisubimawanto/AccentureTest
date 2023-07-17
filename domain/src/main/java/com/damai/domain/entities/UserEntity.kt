package com.damai.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by damai007 on 17/July/2023
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val since: Int?,
    val username: String?,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String?,
    val url: String?,
    val name: String?,
    val company: String?,
    val blog: String?,
    val location: String?,
    val email: String?,
    val bio: String?,
    @ColumnInfo(name = "twitter_username") val twitterUsername: String?,
    @ColumnInfo(name = "public_repos") val publicRepos: Int,
    @ColumnInfo(name = "public_gists") val publicGists: Int,
    val followers: Int,
    val following: Int,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String
)
