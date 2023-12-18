package com.damai.data.mappers

import com.damai.base.BaseMapper
import com.damai.domain.entities.UserFavoriteEntity
import com.damai.domain.models.UserDetailsModel

/**
 * Created by damai007 on 18/December/2023
 */
class UserFavoriteEntityToUserDetailsModelMapper : BaseMapper<UserFavoriteEntity, UserDetailsModel>() {

    override fun map(value: UserFavoriteEntity): UserDetailsModel {
        return UserDetailsModel(
            username = value.username,
            id = value.id,
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
            publicRepos = value.publicRepos,
            publicGists = value.publicGists,
            followers = value.followers,
            following = value.following,
            createdAt = value.createdAt,
            updatedAt = value.updatedAt
        )
    }
}