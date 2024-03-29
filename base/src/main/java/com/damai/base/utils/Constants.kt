package com.damai.base.utils

/**
 * Created by damai007 on 16/December/2023
 */
object Constants {

    const val BASE_URL = "https://api.github.com"

    const val HEADER_ACCEPT_NAME = "Accept"
    const val HEADER_ACCEPT_VALUE = "application/vnd.github+json"
    const val HEADER_API_VERSION_NAME = "X-GitHub-Api-Version"
    const val HEADER_API_VERSION_VALUE = "2022-11-28"
    const val HEADER_AUTHORIZATION_NAME = "Authorization"
    const val HEADER_AUTHORIZATION_VALUE = "Bearer <ADD YOUR GITHUB AUTH TOKEN>"
    const val HEADER_LINK_NAME = "Link"

    const val QUERY_PARAM_SINCE = "since"

    const val BUNDLE_ARGS_USERNAME = "username"
    const val BUNDLE_ARGS_USER_ID = "user_id"

    const val PREFERENCE_KEY_USER_LIST_LAST_TIME_UPDATED = "keyUserListLastTimeUpdated"

    const val TAG_REMOTE_MEDIATOR = "UserListRemote"

    val NEXT_PATTERN = "(?<=<)(\\S*)(?=>; rel=\"next\")".toRegex()

    const val INITIAL_LOAD_SIZE = 50
    const val PER_PAGE_SIZE = 25
    const val PREFETCH_DISTANCE = 5
}