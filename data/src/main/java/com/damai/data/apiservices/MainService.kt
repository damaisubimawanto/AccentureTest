package com.damai.data.apiservices

import com.damai.data.responses.UserDetailsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by damai007 on 11/July/2023
 */
interface MainService {

    @GET("/users")
    suspend fun getUserList(
        @Query("since") since: Int?
    ): Call<List<UserDetailsResponse>>

    @GET("users/{username}")
    suspend fun getUserDetails(
        @Path("username") username: String
    ): Call<UserDetailsResponse>
}