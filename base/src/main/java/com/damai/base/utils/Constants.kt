package com.damai.base.utils

/**
 * Created by damai007 on 11/July/2023
 */
object Constants {

    const val BASE_URL = "https://api.github.com"

    const val HEADER_ACCEPT_NAME = "Accept"
    const val HEADER_ACCEPT_VALUE = "application/vnd.github+json"
    const val HEADER_API_VERSION_NAME = "X-GitHub-Api-Version"
    const val HEADER_API_VERSION_VALUE = "2022-11-28"
    const val HEADER_AUTHORIZATION_NAME = "Authorization"
    const val HEADER_AUTHORIZATION_VALUE = "Bearer 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"
    const val HEADER_LINK_NAME = "Link"

    const val QUERY_PARAM_SINCE = "since"

    const val BUNDLE_ARGS_USERNAME = "username"

    val NEXT_PATTERN = "(?<=<)(\\S*)(?=>; rel=\"next\")".toRegex()
}