package com.example.pssmobile.retrofit

import UserList
import com.example.pssmobile.data.model.LoginService
import retrofit2.http.GET
import retrofit2.http.Query

interface GetUsersApi {

/*
    @GET("User/GetUsers?roleId=2")
    suspend fun getUsersList(): UserList*/

    @GET("User/GetUsers?")
    suspend fun getUsersList(
        @Query("roleId") roleId: String,
    ) : UserList
}