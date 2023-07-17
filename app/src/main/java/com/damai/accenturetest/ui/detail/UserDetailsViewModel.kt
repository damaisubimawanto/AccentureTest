package com.damai.accenturetest.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.damai.base.coroutines.DispatcherProvider
import com.damai.base.extensions.asLiveData
import com.damai.base.networks.Resource
import com.damai.base.utils.Event
import com.damai.domain.models.UserDetailsModel
import com.damai.domain.usecases.UserDetailsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by damai007 on 12/July/2023
 */
class UserDetailsViewModel @Inject constructor(
    private val userDetailsUseCase: UserDetailsUseCase,
    private val dispatcherProvider: DispatcherProvider,
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
    var clickedUsername: String? = null
    //endregion `Variables`

    fun getUserDetails() {
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
                        resource.errorMessage?.let { _errorMessage ->
                            Event(_errorMessage).let(_userDetailsErrorLiveData::postValue)
                        }
                    }
                }
            }
        }
    }

    fun setUserFavorite() {
        val isCurrentFavorite = _userFavoritedLiveData.value ?: false

        // TODO: Save or delete the user into / from favorite local database (Room).

        (isCurrentFavorite.not()).let(_userFavoritedLiveData::setValue)
    }
}