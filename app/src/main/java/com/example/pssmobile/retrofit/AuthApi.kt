package com.example.pssmobile.retrofit

import com.example.pssmobile.data.model.LoginService
import retrofit2.http.*

interface AuthApi {

    @GET("login/GetLogin?")
    suspend fun login(
            @Query("login") email: String,
            @Query("pwd") password: String
    ) : LoginService
}