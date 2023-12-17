package com.damai.base

/**
 * Created by damai007 on 16/December/2023
 */
abstract class BaseMapper<in T, out R> {

    abstract fun map(value: T): R

    fun map(value: List<T>): List<R> {
        return value.map {
            map(value = it)
        }
    }
}