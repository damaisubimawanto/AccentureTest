package com.damai.data.mappers

import com.damai.base.BaseMapper
import com.damai.domain.entities.UserEntity
import com.damai.domain.models.UserDetailsModel

/**
 * Created by damai007 on 18/July/2023
 */
class UserEntityToUserDetailsModelMapper : BaseMapper<UserEntity, UserDetailsModel>() {

    override fun map(value: UserEntity): UserDetailsModel {
        return UserDetailsModel(
            username = value.username,
            id = value.id,
            avatarUrl = value.avatarUrl,
            url = value.url,
            name = value.name,
            company = value.company,
            blog = value.blog,
            location = value.location,
            email = value.email,
            bio = value.bio,
            twitterUsername = value.twitterUsername,
            publicRepos = value.publicRepos,
            publicGists = value.publicGists,
            followers = value.followers,
            following = value.following,
            createdAt = value.createdAt,
            updatedAt = value.updatedAt
        )
    }
}