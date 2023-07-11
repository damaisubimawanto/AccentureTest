package com.damai.base.coroutines

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by damai007 on 11/July/2023
 */
interface DispatcherProvider {

    fun main(): CoroutineDispatcher

    fun default(): CoroutineDispatcher

    fun io(): CoroutineDispatcher
}