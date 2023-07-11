package com.damai.data.apiservices

import com.damai.data.responses.UserDetailsResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by damai007 on 11/July/2023
 */
interface MainService {

    @GET("/users")
    fun getUserListAsync(
        @Query("since") since: Int?
    ): Deferred<Response<List<UserDetailsResponse>>>

    @GET("users/{username}")
    fun getUserDetailsAsync(
        @Path("username") username: String
    ): Deferred<Response<UserDetailsResponse>>
}