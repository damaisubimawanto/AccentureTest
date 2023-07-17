package com.damai.accenturetest.application

import android.app.Application
import com.damai.accenturetest.dagger.AppModules
import com.damai.accenturetest.dagger.DaggerApplicationComponent

/**
 * Created by damai007 on 11/July/2023
 */
class MyApplication : Application() {

    val appComponent by lazy {
        DaggerApplicationComponent.builder()
            .appModule(AppModules(this))
            .build()
    }
}