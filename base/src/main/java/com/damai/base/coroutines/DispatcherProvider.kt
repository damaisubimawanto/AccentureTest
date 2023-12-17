package com.damai.base.coroutines

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by damai007 on 16/December/2023
 */
interface DispatcherProvider {

    fun main(): CoroutineDispatcher

    fun default(): CoroutineDispatcher

    fun io(): CoroutineDispatcher
}