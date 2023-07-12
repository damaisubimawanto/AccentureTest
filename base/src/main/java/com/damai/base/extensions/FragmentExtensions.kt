package com.damai.base.extensions

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.damai.base.utils.Event
import com.damai.base.utils.EventObserver

/**
 * Created by damai007 on 11/July/2023
 */

fun <T> Fragment.observe(
    liveData: LiveData<T>,
    action: (t: T) -> Unit
) {
    with(viewLifecycleOwner) {
        liveData.observe(this) { it?.let { t -> action(t) } }
    }
}

@JvmName("observeEvent")
fun <T> Fragment.observe(
    liveData: LiveData<Event<T>>,
    observer: EventObserver<T>
) {
    with(viewLifecycleOwner) { liveData.observe(this, observer) }
}

fun Fragment.navigateTo(@IdRes resId: Int, bundle: Bundle? = null) {
    try {
        if (bundle == null) {
            findNavController().navigate(resId)
        } else {
            findNavController().navigate(resId, bundle)
        }
    } catch (e: IllegalArgumentException) {
        debugError(error = e)
    } catch (e: Exception) {
        debugError(error = e)
    }
}

fun Fragment.debugError(message: String? = null, error: Throwable) {
    var tag = this::class.java.simpleName
    if (tag.length > 23) {
        tag = tag.substring(0, 23)
    }
    Log.e(tag, message, error)
}