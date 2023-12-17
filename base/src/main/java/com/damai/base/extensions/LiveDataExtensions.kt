package com.damai.base.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Created by damai007 on 16/December/2023
 */

fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>