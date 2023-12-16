package com.damai.domain.models

import com.damai.base.BaseModel

/**
 * Created by damai007 on 11/July/2023
 */
data class UserDetailsModel(
    val username: String?,
    val id: Int,
    val avatarUrl: String?,
    val url: String?,
    val htmlUrl: String?,
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
) : BaseModel()
