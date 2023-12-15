package com.damai.data.mappers

import com.damai.base.BaseMapper
import com.damai.data.responses.UserDetailsResponse
import com.damai.domain.entities.RemoteKeyEntity

/**
 * Created by damai007 on 15/December/2023
 */
class UserDetailsResponseToRemoteKeyEntityMapper : BaseMapper<UserDetailsResponse, RemoteKeyEntity>() {
    private var mNextKey: Int? = null

    override fun map(value: UserDetailsResponse): RemoteKeyEntity {
        return RemoteKeyEntity(
            label = value.login.orEmpty(),
            nextKey = mNextKey
        )
    }

    fun setNextKey(nextKey: Int?): UserDetailsResponseToRemoteKeyEntityMapper {
        mNextKey = nextKey
        return this
    }
}