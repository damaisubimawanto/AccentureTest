package com.damai.data.mappers

import com.damai.base.BaseMapper
import com.damai.base.extensions.orZero
import com.damai.data.responses.UserDetailsResponse
import com.damai.domain.entities.UserEntity

/**
 * Created by damai007 on 17/July/2023
 */
class UserDetailsResponseToUserEntityMapper : BaseMapper<UserDetailsResponse, UserEntity>() {

    override fun map(value: UserDetailsResponse): UserEntity {
        return UserEntity(
            username = value.login,
            id = value.id.orZero(),
            avatarUrl = value.avatarUrl,
            url = value.url,
            htmlUrl = value.htmlUrl,
            name = value.name,
            company = value.company,
            blog = value.blog,
            location = value.location,
            email = value.email,
            bio = value.bio,
            twitterUsername = value.twitterUsername,
            publicRepos = value.publicRepos.orZero(),
            publicGists = value.publicGists.orZero(),
            followers = value.followers.orZero(),
            following = value.following.orZero(),
            createdAt = value.createdAt.orEmpty(),
            updatedAt = value.updatedAt.orEmpty()
        )
    }
}