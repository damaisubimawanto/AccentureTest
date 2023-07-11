package com.damai.domain.usecases

import com.damai.base.networks.FlowUseCase
import com.damai.base.networks.Resource
import com.damai.domain.models.UserDetailsModel
import com.damai.domain.repositories.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by damai007 on 11/July/2023
 */
class UserDetailsUseCase @Inject constructor(
    private val mainRepository: MainRepository
): FlowUseCase<String, UserDetailsModel>() {

    override suspend fun execute(parameters: String?): Flow<Resource<UserDetailsModel>> {
        return mainRepository.getUserDetails(userName = requireNotNull(parameters))
    }
}