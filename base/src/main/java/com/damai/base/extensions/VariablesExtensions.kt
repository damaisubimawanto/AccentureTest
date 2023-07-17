package com.damai.base.extensions

import android.net.Uri
import com.damai.base.utils.Constants

/**
 * Created by damai007 on 11/July/2023
 */

//region Integer values
fun Int?.orZero() = this ?: 0
//endregion `Integer values`

//region String values
fun String?.nextLinkKey(): Int? {
    return if (this?.contains("rel=\"next\"") == true) {
        val matchResult = Constants.NEXT_PATTERN.find(this)
        val resultValue = matchResult?.value
        resultValue?.let { _resultValue ->
            val nextLinkUri = Uri.parse(_resultValue)
            nextLinkUri.getQueryParameter(Constants.QUERY_PARAM_SINCE)?.toIntOrNull()
        }
    } else null
}
//endregion `String values`