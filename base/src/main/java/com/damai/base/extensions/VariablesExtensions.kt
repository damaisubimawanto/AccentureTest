package com.damai.base.extensions

import android.net.Uri
import com.damai.base.utils.Constants.NEXT_PATTERN
import com.damai.base.utils.Constants.QUERY_PARAM_SINCE

/**
 * Created by damai007 on 16/December/2023
 */

//region Integer values
fun Int?.orZero() = this ?: 0
//endregion `Integer values`

//region String values
fun String?.nextLinkKey(): Int? {
    return if (this?.contains("rel=\"next\"") == true) {
        val matchResult = NEXT_PATTERN.find(this)
        val resultValue = matchResult?.value
        resultValue?.let {
            val nextLinkUri = Uri.parse(it)
            nextLinkUri.getQueryParameter(QUERY_PARAM_SINCE)?.toIntOrNull()
        }
    } else null
}

fun String?.validText() = if (this.isNullOrBlank()) {
    "-"
} else this
//endregion `String values`