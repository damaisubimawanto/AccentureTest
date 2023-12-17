package com.damai.data.repos

import com.damai.base.coroutines.DispatcherProvider
import com.damai.base.networks.NetworkResource
import com.damai.base.networks.Resource
import com.damai.base.utils.Constants.HEADER_LINK_NAME
import com.damai.data.apiservices.MainService
import com.damai.data.mappers.UserDetailsResponseToUserDetailsModelMapper
import com.damai.domain.models.UserDetailsModel
import com.damai.domain.repositories.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by damai007 on 16/December/2023
 */
class MainRepositoryImpl @Inject constructor(
    private val mainService: MainService,
    private val dispatcherProvider: DispatcherProvider,
    private val userDetailsMapper: UserDetailsResponseToUserDetailsModelMapper
) : MainRepository {

    override fun getUserDetails(
        userName: String
    ): Flow<Resource<UserDetailsModel>> {
        return object : NetworkResource<UserDetailsModel>(
            dispatcherProvider = dispatcherProvider
        ) {
            override suspend fun remoteFetch(): UserDetailsModel {
                val response = mainService.getUserDetailsAsync(username = userName).await()
                val headers = response.headers()
                return userDetailsMapper.map(response.body()).also {
                    it.statusCode = response.code()
                    it.headerLink = headers[HEADER_LINK_NAME]
                }
            }
        }.asFlow()
    }
}