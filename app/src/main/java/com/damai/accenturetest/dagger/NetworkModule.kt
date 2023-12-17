package com.damai.accenturetest.dagger

import com.damai.base.utils.Constants.BASE_URL
import com.damai.base.utils.Constants.HEADER_ACCEPT_NAME
import com.damai.base.utils.Constants.HEADER_ACCEPT_VALUE
import com.damai.base.utils.Constants.HEADER_API_VERSION_NAME
import com.damai.base.utils.Constants.HEADER_API_VERSION_VALUE
import com.damai.base.utils.Constants.HEADER_AUTHORIZATION_NAME
import com.damai.base.utils.Constants.HEADER_AUTHORIZATION_VALUE
import com.damai.data.apiservices.MainService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by damai007 on 16/December/2023
 */
@Module
class NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder.addInterceptor {
            val newRequest = it.request()
                .newBuilder()
                .addHeader(HEADER_ACCEPT_NAME, HEADER_ACCEPT_VALUE)
                .addHeader(HEADER_API_VERSION_NAME, HEADER_API_VERSION_VALUE)
                .addHeader(HEADER_AUTHORIZATION_NAME, HEADER_AUTHORIZATION_VALUE)
                .build()
            return@addInterceptor it.proceed(newRequest)
        }
        okHttpBuilder.addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        return okHttpBuilder.build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideMainService(retrofit: Retrofit): MainService {
        return retrofit.create(MainService::class.java)
    }
}