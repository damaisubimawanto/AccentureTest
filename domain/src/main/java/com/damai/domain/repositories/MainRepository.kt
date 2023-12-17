package com.damai.domain.repositories

import com.damai.base.networks.Resource
import com.damai.domain.models.UserDetailsModel
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.Throws

/**
 * Created by damai007 on 16/December/2023
 */
interface MainRepository {

    @Throws(Exception::class)
    fun getUserDetails(
        userName: String
    ): Flow<Resource<UserDetailsModel>>
}