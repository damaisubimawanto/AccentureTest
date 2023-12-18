package com.damai.accenturetest.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.damai.accenturetest.room.AppDatabase
import com.damai.base.coroutines.DispatcherProvider
import com.damai.base.extensions.asLiveData
import com.damai.base.extensions.orZero
import com.damai.base.networks.Resource
import com.damai.base.utils.Event
import com.damai.data.mappers.UserDetailsModelToUserFavoriteEntityMapper
import com.damai.domain.models.UserDetailsModel
import com.damai.domain.usecases.UserDetailsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by damai007 on 16/December/2023
 */
class UserDetailsViewModel @Inject constructor(
    private val database: AppDatabase,
    private val userDetailsUseCase: UserDetailsUseCase,
    private val dispatcherProvider: DispatcherProvider,
    private val userDetailsModelToUserFavoriteEntityMapper: UserDetailsModelToUserFavoriteEntityMapper
) : ViewModel() {

    //region Live Data
    private val _userFavoritedLiveData = MutableLiveData(false)
    val userFavoritedLiveData = _userFavoritedLiveData.asLiveData()

    private val _userDetailsLiveData = MutableLiveData<UserDetailsModel>()
    val userDetailsLiveData = _userDetailsLiveData.asLiveData()

    private val _userDetailsErrorLiveData = MutableLiveData<Event<String>>()
    val userDetailsErrorLiveData = _userDetailsErrorLiveData.asLiveData()
    //endregion `Live Data`

    //region Variables
    private val userFavoriteDao = database.userFavoriteDao()
    var clickedUserId: Int = 0
    var clickedUsername: String? = null
    //endregion `Variables`

    fun getUserDetails() {
        clickedUserId.takeIf { it > 0 } ?: run {
            Event("Duh! Empty user ID!").let(_userDetailsErrorLiveData::setValue)
            return
        }
        clickedUsername.takeIf { it.isNullOrBlank().not() } ?: run {
            Event("Duh! Empty username!").let(_userDetailsErrorLiveData::setValue)
            return
        }

        viewModelScope.launch(dispatcherProvider.io()) {
            userDetailsUseCase(clickedUsername).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.model?.let { dataModel ->
                            _userDetailsLiveData.postValue(dataModel)
                        } ?: run {
                            Event("Duh! Empty nih boss!").let(
                                _userDetailsErrorLiveData::postValue
                            )
                        }
                    }
                    is Resource.Error -> {
                        resource.errorMessage?.let { errorMessage ->
                            Event(errorMessage).let(_userDetailsErrorLiveData::postValue)
                        }
                    }
                }
            }

            val userFavoriteEntity = database.withTransaction {
                userFavoriteDao.getUserFavorite(userId = clickedUserId.orZero())
            }
            (userFavoriteEntity != null).let(_userFavoritedLiveData::postValue)
        }
    }

    fun setUserFavorite() {
        viewModelScope.launch(dispatcherProvider.io()) {
            val isCurrentFavorite = _userFavoritedLiveData.value ?: false
            database.withTransaction {
                if (isCurrentFavorite) {
                    userFavoriteDao.removeUserFavorite(userId = clickedUserId)
                    _userFavoritedLiveData.postValue(false)
                    return@withTransaction
                }

                _userDetailsLiveData.value?.let { userDetailsModel ->
                    userDetailsModelToUserFavoriteEntityMapper.map(userDetailsModel).let { userEntity ->
                        userFavoriteDao.insert(user = userEntity)
                    }
                    _userFavoritedLiveData.postValue(true)
                }
            }
        }
    }
}