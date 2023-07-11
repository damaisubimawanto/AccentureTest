package com.damai.domain.repositories

import com.damai.base.networks.Resource
import com.damai.domain.models.UserDetailsModel
import com.damai.domain.models.UserListModel
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.Throws

/**
 * Created by damai007 on 11/July/2023
 */
interface MainRepository {

    @Throws(Exception::class)
    fun getUserList(
        since: Int? = null
    ): Flow<Resource<UserListModel>>

    @Throws(Exception::class)
    fun getUserDetails(
        userName: String
    ): Flow<Resource<UserDetailsModel>>
}