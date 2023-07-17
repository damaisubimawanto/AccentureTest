package com.damai.base.extensions

import android.content.SharedPreferences
import kotlin.jvm.Throws

/**
 * Created by damai007 on 17/July/2023
 */

@Throws(IllegalArgumentException::class)
inline fun <reified T> SharedPreferences.get(key: String, defaultValue: T): T =
    when (T::class) {
        Boolean::class -> this.getBoolean(key, defaultValue as Boolean) as T
        Float::class -> this.getFloat(key, defaultValue as Float) as T
        Int::class -> this.getInt(key, defaultValue as Int) as T
        Long::class -> this.getLong(key, defaultValue as Long) as T
        String::class -> this.getString(key, defaultValue as String) as T
        else -> throw IllegalArgumentException("defaultValue type not supported")
    }

@Throws(UnsupportedOperationException::class)
fun SharedPreferences.update(value: Pair<Any, String>) {
    val editor = edit()
    when (value.first) {
        is Int -> editor.putInt(value.second, value.first as Int)
        is Boolean -> editor.putBoolean(value.second, value.first as Boolean)
        is Float -> editor.putFloat(value.second, value.first as Float)
        is Long -> editor.putLong(value.second, value.first as Long)
        is String -> editor.putString(value.second, value.first as String)
        else -> throw UnsupportedOperationException("unsupported type of value extension")
    }
    editor.apply()
}