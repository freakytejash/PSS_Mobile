package com.example.pssmobile.retrofit

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {

    @GET("user")
    //suspend fun getUser(): LoginResponse

    @POST("logout")
    suspend fun logout(): ResponseBody

}