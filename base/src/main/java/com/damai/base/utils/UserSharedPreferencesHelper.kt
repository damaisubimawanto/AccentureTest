package com.damai.base.utils

import android.content.SharedPreferences
import com.damai.base.extensions.get
import com.damai.base.extensions.update
import com.damai.base.utils.Constants.PREFERENCE_KEY_USER_LIST_LAST_TIME_UPDATED
import javax.inject.Inject

/**
 * Created by damai007 on 17/July/2023
 */
class UserSharedPreferencesHelper @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    var userListLastTimeUpdated: Long
        get() = sharedPreferences.get(PREFERENCE_KEY_USER_LIST_LAST_TIME_UPDATED, 0L)
        set(value) = sharedPreferences.update(value to PREFERENCE_KEY_USER_LIST_LAST_TIME_UPDATED)
}